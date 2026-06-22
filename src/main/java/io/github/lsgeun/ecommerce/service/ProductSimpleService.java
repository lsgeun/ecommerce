package io.github.lsgeun.ecommerce.service;

import io.github.lsgeun.ecommerce.domain.Product;
import io.github.lsgeun.ecommerce.domain.ProductRepository;
import io.github.lsgeun.ecommerce.domain.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductSimpleService {

    final private ProductRepository productRepository;
    final private ProductDtoMapper productDtoMapper;

    public ProductDto.ReadResponse findProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("없음"));

        return productDtoMapper.toReadResponse(product);
    }

}
