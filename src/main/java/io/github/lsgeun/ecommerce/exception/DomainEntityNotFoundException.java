package io.github.lsgeun.ecommerce.exception;

public class DomainEntityNotFoundException extends BusinessException {

    public DomainEntityNotFoundException(String name, Object id) {
        super(
            ErrorCode.DOMAIN_ENTITY_NOT_FOUND_ERROR,
            generateMessage(name, id)
        );
    }

    private static String generateMessage(String name, Object id) {
        return String.format("도메인: %s, 식별자: %s", name, id);
    }
}
