package io.github.lsgeun.ecommerce.product.presentation;

import io.github.lsgeun.ecommerce.product.domain.Product;
import io.github.lsgeun.ecommerce.product.application.ProductSimpleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/products")
@RestController
public class ProductRestController {

    private final ProductSimpleService productSimpleService;
    private final ProductDtoMapper productDtoMapper;

    @GetMapping("/{number}")
    public ResponseEntity<ProductDto.Read.Response> getProduct(
        @PathVariable @NotNull(message = "상품 번호는 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
        String number
    ) {
        Product product = productSimpleService.getProduct(number);

        return ResponseEntity.ok(productDtoMapper.toReadResponse(product));
    }

    @PostMapping
    public ResponseEntity<ProductDto.Create.Response> createProduct(
        @RequestBody @Valid ProductDto.Create.Request createRequest
    ) {
        Product product = productDtoMapper.toProduct(createRequest);

        Product createdProduct = productSimpleService.createProduct(product);

        return ResponseEntity.ok(productDtoMapper.toCreateResponse(createdProduct));
    }

    @PutMapping
    public ResponseEntity<ProductDto.Update.Response> updateProduct(
        @RequestBody @Valid ProductDto.Update.Request updateRequest
    ) {
        Product product = productDtoMapper.toProduct(updateRequest);

        Product updatedProduct = productSimpleService.updateProduct(product);

        return ResponseEntity.ok(productDtoMapper.toUpdateResponse(updatedProduct));
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<ProductDto.Delete.Response> deleteProduct(
        @PathVariable @NotNull(message = "상품 번호는 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
        String number
    ) {
        Product deletedProduct = productSimpleService.deleteProduct(number);

        return ResponseEntity.ok(productDtoMapper.toDeleteResponse(deletedProduct));
    }
}
