package io.github.lsgeun.ecommerce.exception;

import lombok.Getter;

import java.util.List;

@Getter
public class InvalidDomainFieldException extends BusinessException {

    private final transient List<ErrorResponse.FieldErrorDetail> fieldErrorDetails;

    public InvalidDomainFieldException(
        Class<?> targetClass,
        String field,
        Object inputValue,
        String reason
    ) {
        super(
            ErrorCode.INVALID_INPUT_ERROR,
            generateMessage(
                targetClass,
                field,
                inputValue,
                reason
            )
        );

        this.fieldErrorDetails = List.of(
            ErrorResponse.FieldErrorDetail.builder()
                .field(field)
                .reason(reason)
                .build()
        );
    }

    private static String generateMessage(
        Class<?> targetClass,
        String field,
        Object inputValue,
        String reason
    ) {
        return String.format(
            "도메인: %s, 필드: %s, 값: %s, 이유: %s",
            targetClass.getSimpleName(),
            field,
            inputValue,
            reason
        );
    }
}
