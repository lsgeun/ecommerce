package io.github.lsgeun.ecommerce.infrastructure.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByNumber(String number);
    void deleteByNumber(String number);
    boolean existsByNumber(String Number);
}
