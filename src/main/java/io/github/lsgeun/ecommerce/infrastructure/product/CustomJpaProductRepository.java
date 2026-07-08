package io.github.lsgeun.ecommerce.infrastructure.product;

import io.github.lsgeun.ecommerce.domain.product.Product;

import java.util.Optional;

public interface CustomJpaProductRepository {

    public Optional<Product> findByNumberWithCache(String number);
}
