package io.github.lsgeun.ecommerce.product.infrastructure;

import io.github.lsgeun.ecommerce.product.domain.Product;

import java.util.Optional;

public interface ProductJpaCustomRepository {

    Optional<Product> findByNumberWithCache(String number);
}
