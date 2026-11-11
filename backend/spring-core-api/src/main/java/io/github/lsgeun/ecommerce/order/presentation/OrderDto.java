package io.github.lsgeun.ecommerce.order.presentation;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.util.List;

public interface OrderDto {

    interface Create {

        @Value
        class Request {
            @NotNull(message = "사용자 식별자는 필수입니다.")
            Long userId;

            @NotEmpty(message = "주문 상품은 1개 이상이어야 합니다.")
            @Valid
            List<Item> items;

            @Value
            public static class Item {
                @NotNull(message = "상품 번호는 필수입니다.")
                String productNumber;

                @Min(value = 1, message = "주문 수량은 1 이상이어야 합니다.")
                int quantity;
            }
        }
    }

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class Response {
        Long id;
        Long userId;
        String status;
        long totalPrice;
        List<ItemResponse> items;

        @Builder
        private Response(
            Long id, Long userId, String status, long totalPrice, List<ItemResponse> items
        ) {
            this.id = id;
            this.userId = userId;
            this.status = status;
            this.totalPrice = totalPrice;
            this.items = items;
        }
    }

    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ItemResponse {
        String productNumber;
        String productName;
        long unitPrice;
        int quantity;
        long subtotal;

        @Builder
        private ItemResponse(
            String productNumber, String productName, long unitPrice, int quantity, long subtotal
        ) {
            this.productNumber = productNumber;
            this.productName = productName;
            this.unitPrice = unitPrice;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
    }
}
