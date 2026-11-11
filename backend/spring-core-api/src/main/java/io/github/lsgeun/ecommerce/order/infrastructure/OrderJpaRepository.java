package io.github.lsgeun.ecommerce.order.infrastructure;

import io.github.lsgeun.ecommerce.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {
}
