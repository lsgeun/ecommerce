package io.github.lsgeun.ecommerce.order.application;

import io.github.lsgeun.ecommerce.order.domain.Order;
import io.github.lsgeun.ecommerce.order.domain.OrderItem;
import io.github.lsgeun.ecommerce.order.domain.OrderRepository;
import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderSimpleService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 재고 차감과 주문 저장을 한 트랜잭션으로 묶어 중간에 실패하면 둘 다 롤백되도록 함
    @Transactional
    public Order createOrder(Long userId, List<OrderItemCommand> commands) {
        List<OrderItem> items = commands.stream()
            // 상품 번호 순서대로 락을 잡아 서로 다른 주문이 교차해서 잠그는 데드락을 막음
            .sorted(Comparator.comparing(OrderItemCommand::productNumber))
            .map(this::createOrderItem)
            .toList();

        return orderRepository.save(Order.create(userId, items));
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orderRepository.getById(id);
    }

    // 주문 상태 변경과 재고 복구를 한 트랜잭션으로 묶어 둘 중 하나만 반영되는 일을 막음
    // 낙관적 락 충돌은 커밋 시점에 이 메서드 밖으로 던져지므로 재시도는 호출하는 쪽에서 한다
    @Transactional
    public Order cancelOrder(Long id) {
        Order order = orderRepository.getById(id);

        order.cancel();

        order.getItems().stream()
            // 주문 생성과 같은 순서로 락을 잡아 서로 다른 트랜잭션이 교차해서 잠그는 데드락을 막음
            .sorted(Comparator.comparing(OrderItem::getProductNumber))
            .forEach(item -> productRepository
                .getByNumberForUpdate(item.getProductNumber())
                .increaseStock(item.getQuantity()));

        return order;
    }

    private OrderItem createOrderItem(OrderItemCommand command) {
        Product product = productRepository.getByNumberForUpdate(command.productNumber());

        product.decreaseStock(command.quantity());

        return OrderItem.create(
            product.getNumber(),
            product.getName(),
            product.getPrice(),
            command.quantity()
        );
    }
}
