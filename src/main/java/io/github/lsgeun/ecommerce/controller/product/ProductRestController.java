package io.github.lsgeun.ecommerce.controller.product;

import io.github.lsgeun.ecommerce.service.product.ProductSimpleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Pro가ductDto.CreateResponse> createProduct(
        @RequestBody ProductDto.CreateRequest createRequest
    ) {
        ProductDto.CreateResponse createResponse = productSimpleService.createProduct(createRequest);
        return ResponseEntity.ok(createResponse);
    }

    // todo update, delete 컨트롤러 메서드 만들기
}
