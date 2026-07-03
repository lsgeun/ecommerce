package io.github.lsgeun.ecommerce.controller.product;

import io.github.lsgeun.ecommerce.service.product.ProductSimpleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(path = "product")
@RestController
public class ProductRestController {

    private final ProductSimpleService productSimpleService;

    @GetMapping("{number}")
    public ResponseEntity<ProductDto.ReadResponse> getProduct(
        @PathVariable String number
    ) {
        ProductDto.ReadResponse readResponse = productSimpleService.getProduct(number);
        return ResponseEntity.ok(readResponse);
    }

    @PostMapping
    public ResponseEntity<ProductDto.CreateResponse> createProduct(
        @RequestBody ProductDto.CreateRequest createRequest
    ) {
        ProductDto.CreateResponse createResponse = productSimpleService.createProduct(createRequest);
        return ResponseEntity.ok(createResponse);
    }
}
