package io.github.lsgeun.ecommerce.order.infrastructure;

import io.github.lsgeun.ecommerce.common.exception.business.DomainEntityNotFoundException;
import io.github.lsgeun.ecommerce.order.domain.Order;
import io.github.lsgeun.ecommerce.order.domain.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class OrderRepositoryAdapter implements OrderRepository {

    private final OrderJpaRepository orderJpaRepository;

    @Override
    public Order create(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public Order getById(Long id) {
        return orderJpaRepository.findById(id)
            .orElseThrow(() -> new DomainEntityNotFoundException("Order", id));
    }
}
