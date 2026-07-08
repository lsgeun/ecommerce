package io.github.lsgeun.ecommerce.service.product;

import io.github.lsgeun.ecommerce.controller.product.ProductDto;
import io.github.lsgeun.ecommerce.controller.product.ProductDtoMapper;
import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ProductSimpleService {

    private final ProductRepository productRepository;
    private final ProductDtoMapper productDtoMapper;

    @Transactional(readOnly = true)
    public ProductDto.ReadResponse getProduct(
        String number
    ) {
        Product.validateNumber(number);

        Product product = productRepository.getByNumber(number);

        return productDtoMapper.toReadResponse(product);
    }

    @Transactional
    public ProductDto.CreateResponse createProduct(
        ProductDto.CreateRequest createRequest
    ) {
        Product product = productDtoMapper.toProduct(createRequest);

        Product createdProduct = productRepository.create(product);

        return productDtoMapper.toCreateResponse(createdProduct);
    }
}
