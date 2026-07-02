package io.github.lsgeun.ecommerce.exception;

public class InvalidInputException extends BusinessException {
  public InvalidInputException(Class<?> targetClass, String fieldName, Object inputValue, String reason) {
    super(ErrorCode.INVALID_INPUT_ERROR, generateMessage(targetClass, fieldName, inputValue, reason));
  }

  private static String generateMessage(Class<?> targetClass, String fieldName, Object inputValue, String reason) {
    return String.format("도메인: %s, 필드: %s, 값: %s, 이유 %s", targetClass.getSimpleName(), fieldName, inputValue, reason);
  }
}
