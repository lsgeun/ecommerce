package io.github.lsgeun.ecommerce.exception;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorDto {

    private String message;
    private String errorCodeSpec;
}
