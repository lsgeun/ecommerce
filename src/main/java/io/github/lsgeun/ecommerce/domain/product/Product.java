package io.github.lsgeun.ecommerce.domain.product;

import jakarta.persistence.*;
import lombok.Getter;

@Table(
    uniqueConstraints = {
        @UniqueConstraint(
            name = "product_number_index",
            columnNames = "product_number"
        )
    }
)
@Getter
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
