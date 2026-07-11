package io.github.lsgeun.ecommerce.domain.product.exception;

import io.github.lsgeun.ecommerce.exception.BusinessException;
import io.github.lsgeun.ecommerce.exception.ErrorCodeSpec;

public abstract class ProductException extends BusinessException {

    protected ProductException(ErrorCodeSpec errorCodeSpec) {
        super(errorCodeSpec);
    }

    protected ProductException(
        ErrorCodeSpec errorCodeSpec,
        String detailMessage
    ) {
        super(
            errorCodeSpec,
            detailMessage
        );
    }
}
