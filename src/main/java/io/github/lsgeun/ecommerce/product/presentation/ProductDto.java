package io.github.lsgeun.ecommerce.product.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.github.lsgeun.ecommerce.product.domain.ProductStatus;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

public interface ProductDto {

    interface Read {

        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        class Response {
            String number;
            String name;
            int price;
            int stock;
            ProductStatus status;

            @Builder
            private Response(
                String number, String name, int price, int stock, ProductStatus status
            ) {
                this.number = number;
                this.name = name;
                this.price = price;
                this.stock = stock;
                this.status = status;
            }
        }
    }

    interface Create {

        @Value
        class Request {
            @NotNull(message = "상품 번호는 필수입니다.")
            @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
            String number;

            @NotNull(message = "상품 이름은 필수입니다.")
            @Size(min = 2, max = 50, message = "상품 이름은 2자 이상 50자 이하이어야 합니다.")
            String name;

            @Min(value = 0, message = "상품 가격은 0 이상이어야 합니다.")
            int price;

            @Min(value = 0, message = "상품 재고는 0 이상이어야 합니다.")
            int stock;

            @NotNull(message = "상품 상태는 필수입니다.")
            ProductStatus status;
        }

        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        class Response {
            String number;
            String name;
            int price;
            int stock;
            ProductStatus status;

            @Builder
            private Response(
                String number, String name, int price, int stock, ProductStatus status
            ) {
                this.number = number;
                this.name = name;
                this.price = price;
                this.stock = stock;
                this.status = status;
            }
        }
    }

    interface Update {

        @Value
        class Request {
            @NotNull(message = "상품 번호는 필수입니다.")
            @Size(min = 2, max = 50, message = "상품 번호는 2자 이상 50자 이하이어야 합니다.")
            String number;

            @NotNull(message = "상품 이름은 필수입니다.")
            @Size(min = 2, max = 50, message = "상품 이름은 2자 이상 50자 이하이어야 합니다.")
            String name;

            @Min(value = 0, message = "상품 가격은 0 이상이어야 합니다.")
            int price;

            @Min(value = 0, message = "상품 재고는 0 이상이어야 합니다.")
            int stock;

            @NotNull(message = "상품 상태는 필수입니다.")
            ProductStatus status;
        }

        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        class Response {
            String number;
            String name;
            int price;
            int stock;
            ProductStatus status;

            @Builder
            private Response(
                String number, String name, int price, int stock, ProductStatus status
            ) {
                this.number = number;
                this.name = name;
                this.price = price;
                this.stock = stock;
                this.status = status;
            }
        }
    }

    interface Delete {

        @Value
        @JsonInclude(JsonInclude.Include.NON_NULL)
        class Response {
            String number;
            String name;
            int price;
            int stock;
            ProductStatus status;

            @Builder
            private Response(
                String number, String name, int price, int stock, ProductStatus status
            ) {
                this.number = number;
                this.name = name;
                this.price = price;
                this.stock = stock;
                this.status = status;
            }
        }
    }
}
