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

        return orderRepository.create(Order.create(userId, items));
    }

    @Transactional(readOnly = true)
    public Order getOrder(Long id) {
        return orderRepository.getById(id);
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
