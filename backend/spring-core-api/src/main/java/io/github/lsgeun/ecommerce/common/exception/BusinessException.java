package io.github.lsgeun.ecommerce.common.exception;

import io.github.lsgeun.ecommerce.common.exception.code.ErrorCodeSpec;
import lombok.Getter;

@Getter
public abstract class BusinessException extends RuntimeException {

    private final transient ErrorCodeSpec errorCodeSpec;

    protected BusinessException(ErrorCodeSpec errorCodeSpec) {
        super(errorCodeSpec.getMessage());
        this.errorCodeSpec = errorCodeSpec;
    }

    protected BusinessException(ErrorCodeSpec errorCodeSpec, String detailMessage) {
        super(String.format("%s (%s)", errorCodeSpec.getMessage(), detailMessage));
        this.errorCodeSpec = errorCodeSpec;
    }
}
