package io.github.lsgeun.ecommerce.domain.product;

import io.github.lsgeun.ecommerce.exception.InvalidDomainFieldException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.NaturalId;

import java.util.Objects;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
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
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @NaturalId
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

    @Builder(access = AccessLevel.PRIVATE)
    private Product(
        Long id,
        String number,
        String name,
        long price,
        int stock,
        ProductStatus status
    ) {
        this.id = id;
        this.number = number;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.status = status;
    }

    public static Product create(
        String number,
        String name,
        long price,
        int stock,
        ProductStatus status
    ) {
        validateNumber(number);
        validateName(name);
        validatePrice(price);
        validateStock(stock);
        validateStatus(status);

        return Product.builder()
            .id(null)
            .number(number)
            .name(name)
            .price(price)
            .stock(stock)
            .status(status)
            .build();
    }

    public static void validateNumber(String number) {
        if (Objects.isNull(number)) {
            throw new InvalidDomainFieldException(
                Product.class,
                "number",
                null,
                ""
            );
        }

        boolean isValid =
            (2 <= number.length() && number.length() <= 50);
        if (!isValid) {
            throw new InvalidDomainFieldException(
                Product.class,
                "number",
                number,
                "문자열이 2 ~ 50자 이어야 합니다"
            );
        }
    }

    public static void validateName(String name) {
        if (Objects.isNull(name)) {
            throw new InvalidDomainFieldException(
                Product.class,
                "name",
                null,
                ""
            );
        }

        boolean isValid =
            (2 <= name.length() && name.length() <= 50);
        if (!isValid) {
            throw new InvalidDomainFieldException(
                Product.class,
                "name",
                name,
                ""
            );
        }
    }

    public static void validatePrice(long price) {
        boolean isValid = (price >= 0);
        if (!isValid) {
            throw new InvalidDomainFieldException(
                Product.class,
                "price",
                price,
                ""
            );
        }
    }

    public static void validateStock(int stock) {
        boolean isValid = (stock >= 0);
        if (!isValid) {
            throw new InvalidDomainFieldException(
                Product.class,
                "stock",
                stock,
                ""
            );
        }
    }

    public static void validateStatus(ProductStatus status) {
        if (Objects.isNull(status)) {
            throw new InvalidDomainFieldException(
                Product.class,
                "status",
                null,
                ""
            );
        }
    }
}
