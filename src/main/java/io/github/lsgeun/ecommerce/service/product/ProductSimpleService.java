package io.github.lsgeun.ecommerce.service.product;

import io.github.lsgeun.ecommerce.controller.product.ProductDto;
import io.github.lsgeun.ecommerce.controller.product.ProductDtoMapper;
import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.ProductRepository;
import io.github.lsgeun.ecommerce.exception.DomainEntityAlreadyExistsException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Validated
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
        @Valid
        ProductDto.CreateRequest createRequest
    ) {
        Product product = productDtoMapper.toProduct(createRequest);

        if (productRepository.existsByNumber(product.getNumber())) {
            throw new DomainEntityAlreadyExistsException("Product", product.getNumber());
        }

        Product createdProduct = productRepository.create(product);

        return productDtoMapper.toCreateResponse(createdProduct);
    }

    @Transactional
    public ProductDto.UpdateResponse updateProduct(
        @Valid
        ProductDto.UpdateRequest updateRequest
    ) {
        Product product = productRepository.getByNumber(
            updateRequest.getNumber()
        );

        productDtoMapper.updateFromDto(updateRequest, product);

        return productDtoMapper.toUpdateResponse(product);
    }

    @Transactional
    public ProductDto.DeleteResponse deleteProduct(
        String number
    ) {
        Product.validateNumber(number);

        Product product = productRepository.deleteByNumber(number);

        return productDtoMapper.toDeleteResponse(product);
    }
}
