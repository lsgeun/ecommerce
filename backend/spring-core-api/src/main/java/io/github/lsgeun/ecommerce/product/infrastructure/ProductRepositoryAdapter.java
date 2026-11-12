package io.github.lsgeun.ecommerce.product.infrastructure;

import io.github.lsgeun.ecommerce.common.exception.business.DomainEntityNotFoundException;
import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Product getByNumberForUpdate(String number) {
        return productJpaRepository.findByNumberForUpdate(number)
            .orElseThrow(() -> new DomainEntityNotFoundException("Product", number));
    }
}
