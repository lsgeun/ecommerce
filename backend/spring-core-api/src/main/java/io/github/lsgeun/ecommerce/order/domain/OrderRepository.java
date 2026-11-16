package io.github.lsgeun.ecommerce.order.domain;

public interface OrderRepository {

    Order save(Order order);

    Order getById(Long id);
}
