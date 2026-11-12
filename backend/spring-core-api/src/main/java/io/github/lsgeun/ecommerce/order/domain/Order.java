package io.github.lsgeun.ecommerce.order.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import io.github.lsgeun.ecommerce.common.exception.business.InvalidDomainFieldException;

@Entity
@Table(name = "order_tb")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status;

    // 주문이 없으면 항목도 의미가 없으므로 주문과 생명주기를 함께 관리
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id", nullable = false)
    private List<OrderItem> items = new ArrayList<>();

    private Order(Long userId, List<OrderItem> items) {
        validateUserId(userId);
        validateItems(items);

        this.userId = userId;
        this.items = new ArrayList<>(items);
        this.status = OrderStatus.CREATED;
    }

    public static Order create(Long userId, List<OrderItem> items) {
        return new Order(userId, items);
    }

    public long getTotalPrice() {
        return items.stream()
            .mapToLong(OrderItem::getSubtotal)
            .sum();
    }

    private static void validateUserId(Long userId) {
        if (userId == null) {
            throw new InvalidDomainFieldException(
                Order.class, "userId", userId, "사용자 식별자는 필수입니다."
            );
        }
    }

    private static void validateItems(List<OrderItem> items) {
        if (items == null || items.isEmpty()) {
            throw new InvalidDomainFieldException(
                Order.class, "items", items, "주문 상품은 1개 이상이어야 합니다."
            );
        }
    }
}
