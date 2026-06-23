package io.github.lsgeun.ecommerce.controller;

import io.github.lsgeun.ecommerce.domain.product.ProductDto;
import io.github.lsgeun.ecommerce.service.ProductSimpleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping(path = "product")
@RestController
public class ProductRestController {

    final private ProductSimpleService productSimpleService;

    @GetMapping("{id}")
    public ResponseEntity<ProductDto.ReadResponse> findProduct(@PathVariable Long id) {
        ProductDto.ReadResponse readResponse = productSimpleService.findProductById(id);
        return ResponseEntity.ok(readResponse);
    }
}
