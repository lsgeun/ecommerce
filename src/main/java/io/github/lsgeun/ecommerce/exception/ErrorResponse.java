package io.github.lsgeun.ecommerce.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
    String message,
    String code,
    int status,

    @JsonFormat(
        shape = JsonFormat.Shape.STRING,
        pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS",
        timezone = "Asia/Seoul"
    )
    LocalDateTime timestamp,

    List<FieldErrorDetail> errors
) {

    private static final Logger log =
        LoggerFactory.getLogger(ErrorResponse.class);
    private static final ZoneId SEOUL_ZONE = ZoneId.of("Asia/Seoul");

    @Builder(access = AccessLevel.PRIVATE)
    public ErrorResponse {
        if (timestamp == null) {
            log.error("[ERROR] 에러 응답의 타임스탬프가 누락되었습니다.");

            timestamp = LocalDateTime.now(SEOUL_ZONE);
        }

        if (message == null || message.isBlank() ||
            code == null || code.isBlank() ||
            status < 100 || status > 599) {

            log.error("[ERROR] 에러 응답의 메시지, 코드, 상태 중 하나 누락되어 기본값으로 대체합니다.\nmessage: {}, code: {}, status: {}", message, code, status);

            message = ErrorCode.UNEXPECTED_SERVER_ERROR.getMessage();
            code = ErrorCode.UNEXPECTED_SERVER_ERROR.getCode();
            status = ErrorCode.UNEXPECTED_SERVER_ERROR.getHttpStatus().value();
        }

        if (errors != null) {
            // 불변 리스트로 변환
            errors = List.copyOf(errors);
        }
    }

    public static ErrorResponse from(
        ErrorCodeSpec errorCodeSpec
    ) {
        LocalDateTime time = getCurrentTime(SEOUL_ZONE);

        errorCodeSpec = resolveErrorCodeSpec(errorCodeSpec, time);

        return builder()
            .message(errorCodeSpec.getMessage())
            .code(errorCodeSpec.getCode())
            .status(errorCodeSpec.getHttpStatus().value())
            .timestamp(time)
            .build();
    }

    public static ErrorResponse of(
        ErrorCodeSpec errorCodeSpec,
        List<FieldErrorDetail> errors
    ) {
        LocalDateTime time = getCurrentTime(SEOUL_ZONE);

        errorCodeSpec = resolveErrorCodeSpec(errorCodeSpec, time);

        return builder()
            .message(errorCodeSpec.getMessage())
            .code(errorCodeSpec.getCode())
            .status(errorCodeSpec.getHttpStatus().value())
            .timestamp(time)
            .errors(errors)
            .build();
    }

    private static LocalDateTime getCurrentTime(ZoneId zoneId) {
        return LocalDateTime.now(zoneId);
    }

    private static ErrorCodeSpec resolveErrorCodeSpec (
        ErrorCodeSpec errorCodeSpec,
        LocalDateTime time
    ) {
        if (errorCodeSpec == null) {
            errorCodeSpec = ErrorCode.UNEXPECTED_SERVER_ERROR;

            log.error(
                "[ERROR] ErrorCodeSpec이 null입니다. 발생시각: {}",
                time
            );
        }

        return errorCodeSpec;
    }

    public record FieldErrorDetail(
        String field,
        String reason
    ) {

        @Builder
        public FieldErrorDetail {
            // 내부 레코드에서 Lombok @Builder를 사용하기 위한 빈 컴팩트 생성자
        }
    }
}
