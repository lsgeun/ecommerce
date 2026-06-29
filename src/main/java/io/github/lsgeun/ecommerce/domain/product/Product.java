package io.github.lsgeun.ecommerce.domain.product;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(
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
    @Column(name = "product_number", nullable = false)
    private String number;
    @Column(name = "product_name", nullable = false)
    private String name;
    @Column
    private int price;
    @Column
    private int stock;
}
