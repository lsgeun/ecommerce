package io.github.lsgeun.ecommerce.product.infrastructure;

import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductRepository;
import io.github.lsgeun.ecommerce.global.exception.DomainEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class ProductRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    @Override
    public Optional<Product> findByNumber(String number) {
        return productJpaRepository.findByNumberWithCache(number);
    }

    @Override
    public Product getByNumber(String number) {
        return this.findByNumber(number)
            .orElseThrow(() -> new DomainEntityNotFoundException("Product", number));
    }

    @Override
    public Product create(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Product update(Product product) {
        return productJpaRepository.save(product);
    }

    @Override
    public Product delete(Product product) {
        return this.deleteByNumber(product.getNumber());
    }

    @Override
    public Product deleteByNumber(String number) {
        Product product = getByNumber(number);

        productJpaRepository.delete(product);

        return product;
    }

    @Override
    public boolean existsByNumber(String number) {
        return productJpaRepository.existsByNumber(number);
    }
}
