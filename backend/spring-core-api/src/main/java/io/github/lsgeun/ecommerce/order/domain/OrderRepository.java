package io.github.lsgeun.ecommerce.order.domain;

public interface OrderRepository {

    Order create(Order order);

    Order getById(Long id);
}
