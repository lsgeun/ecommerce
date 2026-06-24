package io.github.lsgeun.ecommerce.service.product;

import io.github.lsgeun.ecommerce.controller.product.ProductDto;
import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductSimpleService {

    final private ProductRepository productRepository;
    final private ProductDtoMapper productDtoMapper;

    @Transactional(readOnly = true)
    public ProductDto.ReadResponse getProduct(String number) {
        Product product = productRepository.getByNumber(number);
        return productDtoMapper.toReadResponse(product);
    }
    // todo Product 도메인 규칙
    @Transactional
    public ProductDto.CreateResponse createProduct(ProductDto.CreateRequest createRequest) {
        // todo CreateRequest 매퍼
        // Product product = productRepository.create(createRequest);
        // return productDtoMapper.toCreateResponse(product);
        return null;
    }
}
