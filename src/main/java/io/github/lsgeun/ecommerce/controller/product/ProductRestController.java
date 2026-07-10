package io.github.lsgeun.ecommerce.controller.product;

import io.github.lsgeun.ecommerce.service.product.ProductSimpleService;
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
@RequestMapping(path = "/product")
@RestController
public class ProductRestController {

    private final ProductSimpleService productSimpleService;

    @GetMapping("/{number}")
    public ResponseEntity<ProductDto.ReadResponse> getProduct(
        @PathVariable
        @NotNull(message = "상품 번호는 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
        String number
    ) {
        ProductDto.ReadResponse readResponse =
            productSimpleService.getProduct(number);

        return ResponseEntity.ok(readResponse);
    }

    @PostMapping
    public ResponseEntity<ProductDto.CreateResponse> createProduct(
        @RequestBody
        @Valid
        ProductDto.CreateRequest createRequest
    ) {
        ProductDto.CreateResponse createResponse =
            productSimpleService.createProduct(createRequest);

        return ResponseEntity.ok(createResponse);
    }

    @PutMapping
    public ResponseEntity<ProductDto.UpdateResponse> updateProduct(
        @RequestBody
        @Valid
        ProductDto.UpdateRequest updateRequest
    ) {
        ProductDto.UpdateResponse updateResponse =
            productSimpleService.updateProduct(updateRequest);

        return ResponseEntity.ok(updateResponse);
    }

    @DeleteMapping("/{number}")
    public ResponseEntity<ProductDto.DeleteResponse> deleteProduct(
        @PathVariable
        @NotNull(message = "상품 번호는 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
        String number
    ) {
        ProductDto.DeleteResponse deleteResponse =
            productSimpleService.deleteProduct(number);

        return ResponseEntity.ok(deleteResponse);
    }
}
