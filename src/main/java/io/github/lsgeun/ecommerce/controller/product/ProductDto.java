package io.github.lsgeun.ecommerce.controller.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lsgeun.ecommerce.domain.product.ProductStatus;
import lombok.Builder;

public class ProductDto {

    // Read
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ReadResponse(
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {

        @Builder
        public ReadResponse {
            // 내부 레코드에서 Lombok @Builder를 사용하기 위한 빈 컴팩트 생성자
        }
    }

    // Create
    public record CreateRequest(
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record CreateResponse(
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {

        @Builder
        public CreateResponse {
            // 내부 레코드에서 Lombok @Builder를 사용하기 위한 빈 컴팩트 생성자
        }
    }
}
