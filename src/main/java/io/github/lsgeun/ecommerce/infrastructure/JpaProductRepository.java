package io.github.lsgeun.ecommerce.infrastructure;

import io.github.lsgeun.ecommerce.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

}
