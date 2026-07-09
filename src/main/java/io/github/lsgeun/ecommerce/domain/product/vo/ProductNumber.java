package io.github.lsgeun.ecommerce.domain.product.vo;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@EqualsAndHashCode
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductNumber {
    private String value;

    public ProductNumber(String value) {
        this.value = value;
    }

    private void validate(String number) {

    }
}
