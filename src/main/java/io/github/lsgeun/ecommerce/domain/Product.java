package io.github.lsgeun.ecommerce.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column
    private String name;

    @Column
    private int price;

    @Column
    private int stock;
}
