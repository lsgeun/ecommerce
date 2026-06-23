package io.github.lsgeun.ecommerce.infrastructure;

import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.ProductRepository;
import io.github.lsgeun.ecommerce.exception.product.ProductNotFoundException;
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
        // todo product 레코드 존재 유무 확인
        jpaProductRepository.delete(product);
        return product;
    }

    @Override
    public Product deleteByNumber(String number) {
        // todo product 레코드 존재 유무 확인
        jpaProductRepository.deleteByNumber(number);
        return product;
    }

    @Override
    public boolean validateNotExistsByNumber(String number) {
        return jpaProductRepository.existsByNumber(number);
    }
}
