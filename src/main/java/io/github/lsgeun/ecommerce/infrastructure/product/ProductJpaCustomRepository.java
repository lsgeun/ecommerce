package io.github.lsgeun.ecommerce.infrastructure.product;

import io.github.lsgeun.ecommerce.domain.product.Product;

import java.util.Optional;

public interface ProductJpaCustomRepository {

    Optional<Product> findByNumberWithCache(String number);
}
