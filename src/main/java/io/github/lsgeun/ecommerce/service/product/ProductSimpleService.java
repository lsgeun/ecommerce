package io.github.lsgeun.ecommerce.service.product;

import io.github.lsgeun.ecommerce.controller.product.ProductDto;
import io.github.lsgeun.ecommerce.controller.product.ProductDtoMapper;
import io.github.lsgeun.ecommerce.domain.product.Product;
import io.github.lsgeun.ecommerce.domain.product.ProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        @NotNull(message = "상품 번호는 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
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

        Product createdProduct = productRepository.create(product);

        return productDtoMapper.toCreateResponse(createdProduct);
    }
}
