package io.github.lsgeun.ecommerce.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import io.github.lsgeun.ecommerce.common.exception.InvalidDomainFieldException;

@Entity
@Table(name = "order_item_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_number", nullable = false, length = 50)
    private String productNumber;

    @Column(name = "product_name", nullable = false, length = 50)
    private String productName;

    // 주문 시점의 가격을 저장해 이후 상품 가격이 바뀌어도 주문 금액이 유지되도록 함
    @Column(name = "unit_price", nullable = false)
    private long unitPrice;

    @Column(nullable = false)
    private int quantity;

    private OrderItem(String productNumber, String productName, long unitPrice, int quantity) {
        validateQuantity(quantity);

        this.productNumber = productNumber;
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public static OrderItem create(String productNumber, String productName, long unitPrice, int quantity) {
        return new OrderItem(productNumber, productName, unitPrice, quantity);
    }

    public long getSubtotal() {
        return unitPrice * quantity;
    }

    private static void validateQuantity(int quantity) {
        if (quantity < 1) {
            throw new InvalidDomainFieldException(
                OrderItem.class, "quantity", quantity, "주문 수량은 1 이상이어야 합니다."
            );
        }
    }
}
