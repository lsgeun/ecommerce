package io.github.lsgeun.ecommerce.controller.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lsgeun.ecommerce.domain.product.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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
        @NotNull(message = "상품 번호는 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
        String number,

        @NotNull(message = "상품 이름은 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 이름은 2자 이상 50자 이하이어야 합니다.")
        String name,

        @Min(value = 0, message = "상품 가격은 0 이상이어야 합니다.")
        int price,

        @Min(value = 0, message = "상품 재고는 0 이상이어야 합니다.")
        int stock,

        @NotNull(message = "상품 상태는 필수입니다.")
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

    // Update
    public record UpdateRequest(
        @NotNull(message = "상품 번호는 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
        String number,

        @NotNull(message = "상품 이름은 필수입니다.")
        @Size(min = 2, max = 50, message = "상품 이름은 2자 이상 50자 이하이어야 합니다.")
        String name,

        @Min(value = 0, message = "상품 가격은 0 이상이어야 합니다.")
        int price,

        @Min(value = 0, message = "상품 재고는 0 이상이어야 합니다.")
        int stock,

        @NotNull(message = "상품 상태는 필수입니다.")
        ProductStatus status
    ) {}

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record UpdateResponse(
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {

        @Builder
        public UpdateResponse {
            // 내부 레코드에서 Lombok @Builder를 사용하기 위한 빈 컴팩트 생성자
        }
    }

    // DELETE
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record DeleteResponse(
        String number,
        String name,
        int price,
        int stock,
        ProductStatus status
    ) {

        @Builder
        public DeleteResponse {
            // 내부 레코드에서 Lombok @Builder를 사용하기 위한 빈 컴팩트 생성자
        }
    }
}
