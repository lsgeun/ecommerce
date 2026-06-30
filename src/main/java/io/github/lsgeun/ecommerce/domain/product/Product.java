package io.github.lsgeun.ecommerce.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
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
}
