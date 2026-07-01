package io.github.lsgeun.ecommerce.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
    name = "product_tb",
    uniqueConstraints = {
        @UniqueConstraint(
            name = "uk_product_number",
            columnNames = "product_number"
        )
    }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @Column(name = "product_number", nullable = false, length = 50)
    private String number;
    @Column(nullable = false, length = 50)
    private String name;
    @Column(nullable = false)
    private long price;
    @Column(name = "stock_quantity", nullable = false)
    private int stock;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProductStatus status;

    @Builder(access = AccessLevel.PRIVATE, builderMethodName = "builder")
    private Product(Long id, String number, String name, long price, int stock, ProductStatus status) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public static Product create(String number, String name, long price, int stock, ProductStatus status) {
        validateNumber(number);
        validateName(name);
        validatePrice(price);
        validateStock(stock);
        validateStatus(status);

        return Product.builder()
            .id(null)
            .number(number).name(name).price(price).stock(stock).status(status)
            .build();
    }
}
