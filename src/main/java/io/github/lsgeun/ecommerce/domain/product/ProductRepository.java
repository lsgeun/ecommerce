package io.github.lsgeun.ecommerce.domain.product;

import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    Product save(Product product);
}
