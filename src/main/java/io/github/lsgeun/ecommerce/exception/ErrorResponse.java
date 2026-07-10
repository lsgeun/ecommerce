package io.github.lsgeun.ecommerce.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private static final Logger log =
        LoggerFactory.getLogger(ErrorResponse.class);
    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    String message;
    String code;
    int status;

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
        timezone = "Asia/Seoul"
    )
    LocalDateTime timestamp;

    List<FieldErrorDetail> fieldErrorDetails;

    @Builder(access = AccessLevel.PRIVATE)
    private ErrorResponse(
        String message,
        String code,
        int status,
        LocalDateTime timestamp,
        List<FieldErrorDetail> fieldErrorDetails
    ) {
        timestamp = (timestamp == null) ? LocalDateTime.now(SEOUL_ZONE) : timestamp;

        if (message == null || message.isBlank() ||
            code == null || code.isBlank() ||
            status < 100 || status > 599) {

            log.error("에러 응답의 메시지, 코드, 상태 중 하나 누락되어 기본값으로 대체합니다.\nmessage: {}, code: {}, status: {}", message, code, status);

            message = ErrorCode.UNEXPECTED_SERVER_ERROR.getMessage();
            code = ErrorCode.UNEXPECTED_SERVER_ERROR.getCode();
            status = ErrorCode.UNEXPECTED_SERVER_ERROR.getHttpStatus().value();
        }

        if (fieldErrorDetails != null) {
            // 불변 리스트로 변환
            fieldErrorDetails = List.copyOf(fieldErrorDetails);
        }

        this.message = message;
        this.code = code;
        this.status = status;
        this.timestamp = timestamp;
        this.fieldErrorDetails = fieldErrorDetails;
    }

    public static ErrorResponse from(
        ErrorCodeSpec errorCodeSpec
    ) {
        errorCodeSpec = resolveErrorCodeSpec(errorCodeSpec);

        return builder()
            .message(errorCodeSpec.getMessage())
            .code(errorCodeSpec.getCode())
            .status(errorCodeSpec.getHttpStatus().value())
            .build();
    }

    public static ErrorResponse of(
        ErrorCodeSpec errorCodeSpec,
        List<FieldErrorDetail> fieldErrorDetails
    ) {
        errorCodeSpec = resolveErrorCodeSpec(errorCodeSpec);

        return builder()
            .message(errorCodeSpec.getMessage())
            .code(errorCodeSpec.getCode())
            .status(errorCodeSpec.getHttpStatus().value())
            .fieldErrorDetails(fieldErrorDetails)
            .build();
    }

    private static ErrorCodeSpec resolveErrorCodeSpec (
        ErrorCodeSpec errorCodeSpec
    ) {
        if (errorCodeSpec == null) {
            log.error("ErrorCodeSpec이 null입니다.");

            return ErrorCode.UNEXPECTED_SERVER_ERROR;
        }

        return errorCodeSpec;
    }

    @Value
    public static class FieldErrorDetail {

        String field;
        String reason;

        @Builder
        private FieldErrorDetail(
            String field,
            String reason
        ) {
            this.field = field;
            this.reason = reason;
        }
    }
}
