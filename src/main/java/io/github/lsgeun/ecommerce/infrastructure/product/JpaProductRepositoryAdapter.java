package io.github.lsgeun.ecommerce.infrastructure.product;

import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.ProductRepository;
import io.github.lsgeun.ecommerce.domain.product.exception.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaProductRepositoryAdapter implements ProductRepository {

    final private JpaProductRepository jpaProductRepository;

    @Override
    public Optional<Product> findByNumber(String number) {
        return jpaProductRepository.findByNumber(number);
    }

    @Override
    public Product getByNumber(String number) {
        return this.findByNumber(number).orElseThrow(() -> new ProductNotFoundException(number));
    }

    @Override
    public Product create(Product product) {
        return jpaProductRepository.save(product);
    }

    @Override
    public Product delete(Product product) {
        Product productToDelete = getByNumber(product.getNumber());
        jpaProductRepository.delete(productToDelete);
        return productToDelete;
    }

    @Override
    public Product deleteByNumber(String number) {
        Product productToDelete = getByNumber(number);
        return this.delete(productToDelete);
    }

    @Override
    public boolean validateNotExistsByNumber(String number) {
        return jpaProductRepository.existsByNumber(number);
    }
}
