package io.github.lsgeun.ecommerce.product.application;

import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.domain.ProductRepository;
import io.github.lsgeun.ecommerce.global.exception.DomainEntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
@RequiredArgsConstructor
@Service
public class ProductSimpleService {

    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public Product getProduct(String number) {
        Product.validateNumber(number);

        return productRepository.getByNumber(number);
    }

    @Transactional
    public Product createProduct(Product product) {
        if (productRepository.existsByNumber(product.getNumber())) {
            throw new DomainEntityAlreadyExistsException("Product", product.getNumber());
        }

        return productRepository.create(product);
    }

    @Transactional
    public Product updateProduct(Product product) {
        Product productToUpdate = productRepository.getByNumber(product.getNumber());

        productToUpdate.updateFrom(product);

        return productRepository.update(productToUpdate);
    }

    @Transactional
    public Product deleteProduct(String number) {
        Product.validateNumber(number);

        return productRepository.deleteByNumber(number);
    }
}
