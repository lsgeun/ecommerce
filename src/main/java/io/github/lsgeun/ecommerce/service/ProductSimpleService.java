package io.github.lsgeun.ecommerce.service;

import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.ProductRepository;
import io.github.lsgeun.ecommerce.domain.product.ProductDto;
import io.github.lsgeun.ecommerce.exception.product.ProductNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductSimpleService {

    final private ProductRepository productRepository;
    final private ProductDtoMapper productDtoMapper;

    public ProductDto.ReadResponse findProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        return productDtoMapper.toReadResponse(product);
    }
}
