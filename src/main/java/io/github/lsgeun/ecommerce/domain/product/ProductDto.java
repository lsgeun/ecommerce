package io.github.lsgeun.ecommerce.domain.product;

import lombok.Builder;

public class ProductDto {
    @Builder
    public static class ReadResponse {
        Long id;
        String name;
        int price;
        int stock;
    }

    public static class CreateRequest {
        String name;
        int price;
        int stock;
    }
    @Builder
    public static class CreateResponse {
        Long id;
        String name;
        int price;
        int stock;
    }
}
