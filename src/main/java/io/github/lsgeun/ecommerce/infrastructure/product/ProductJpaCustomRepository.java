package io.github.lsgeun.ecommerce.infrastructure.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.QProduct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductJpaCustomRepository {

    private final EntityManager entityManager;

    // TODO: 2026-07-12까지 아래 코드 원리를 대략적으로 이해하고 메모로 남겨두기
    public Optional<Product> findByNumberWithCache(String number) {
        Product product = entityManager.unwrap(Session.class)
            .byNaturalId(Product.class)
            .using(
                QProduct.product.number.getMetadata().getName(),
                number)
            .load();
        return Optional.ofNullable(product);
    }
}
