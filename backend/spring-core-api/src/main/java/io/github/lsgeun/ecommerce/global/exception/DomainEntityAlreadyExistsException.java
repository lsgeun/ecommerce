package io.github.lsgeun.ecommerce.global.exception;

public class DomainEntityAlreadyExistsException extends BusinessException {

    public DomainEntityAlreadyExistsException(String name, Object id) {
        super(
            ErrorCode.DOMAIN_ENTITY_ALREADY_EXISTS_ERROR,
            generateMessage(name, id)
        );
    }

    private static String generateMessage(String name, Object id) {
        return String.format("도메인: %s, 식별자: %s", name, id);
    }
}
