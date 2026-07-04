package io.github.lsgeun.ecommerce.domain.product.exception;

import io.github.lsgeun.ecommerce.exception.ErrorCodeSpec;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ProductErrorCode implements ErrorCodeSpec {

    PRODUCT_NOT_FOUND("상품을 찾을 수 없습니다", "P001", HttpStatus.NOT_FOUND, LogLevel.WARN);

    private final String message;
    private final String code;
    private final HttpStatus httpStatus;
    private final LogLevel logLevel;
}
