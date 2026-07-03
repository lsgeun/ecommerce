package io.github.lsgeun.ecommerce.controller.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lsgeun.ecommerce.domain.product.ProductStatus;
import lombok.Builder;

public class ProductDto {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ReadResponse(
        Long id,
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {
        @Builder(builderMethodName = "builder")
        public ReadResponse { }
    }

    public record CreateRequest(
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {
    }
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record CreateResponse(
        Long id,
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {
        @Builder(builderMethodName = "builder")
        public CreateResponse { }
    }
}
