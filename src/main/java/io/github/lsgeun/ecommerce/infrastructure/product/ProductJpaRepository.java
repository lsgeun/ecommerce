package io.github.lsgeun.ecommerce.infrastructure.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<Product, Long>, ProductJpaCustomRepository {

    boolean existsByNumber(String number);
}
