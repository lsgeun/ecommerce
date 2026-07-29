package io.github.lsgeun.ecommerce.infrastructure.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductJpaCustomRepositoryImpl implements ProductJpaCustomRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<Product> findByNumberWithCache(String number) {
        Product product = entityManager.unwrap(Session.class)
            .byNaturalId(Product.class)
            .using("number", number)
            .load();

        return Optional.ofNullable(product);
    }
}
