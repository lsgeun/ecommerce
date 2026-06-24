package io.github.lsgeun.ecommerce.controller.product;

import lombok.Builder;
import lombok.Getter;

public class ProductDto {
    @Builder
    @Getter
    public static class ReadResponse {
        Long id;
        String number;
        String name;
        int price;
        int stock;
    }

    public static class CreateRequest {
        String number;
        String name;
        int price;
        int stock;
    }
    @Builder
    @Getter
    public static class CreateResponse {
        Long id;
        String number;
        String name;
        int price;
        int stock;
    }
}
