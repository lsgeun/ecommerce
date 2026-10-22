---
updated: 2026-10-22
---

- `application` 패키지의 SimpleService 규칙
- 역할은 유스케이스 진행, 트랜잭션 경계, 저장된 상태에 의존하는 규칙(존재, 중복)

# 클래스

- 이름은 `{Domain}SimpleService`
- 애노테이션은 `@Validated`, `@RequiredArgsConstructor`, `@Service`
- 의존성은 `domain`의 `{Domain}Repository` **인터페이스**를 `private final` 필드로 둠. `{Domain}JpaRepository`나 `{Domain}RepositoryAdapter`를 **절대** 직접 쓰지 않음
- DTO를 **절대** 모름. 엔티티(또는 기본 타입 값)를 받고 엔티티를 반환

# 트랜잭션

- 모든 `public` 메서드에 `@Transactional`을 **반드시** 붙임. `org.springframework.transaction.annotation.Transactional`
- 조회만 하는 메서드는 `@Transactional(readOnly = true)`
- 저장, 수정, 삭제가 있는 메서드는 `@Transactional`

# 메서드

| 동작        | 메서드 이름       | 트랜잭션          | 하는 일                                                         |
| ----------- | ----------------- | ----------------- | --------------------------------------------------------------- |
| 조회        | `getProduct`      | `readOnly = true` | 식별자 검증 후 `repository.getByNumber`                         |
| 생성        | `createProduct`   | 쓰기              | 중복이면 예외, 아니면 `repository.create`                       |
| 수정        | `updateProduct`   | 쓰기              | `getByNumber`로 읽고 `updateFrom`으로 바꾼 뒤 `repository.update` |
| 삭제        | `deleteProduct`   | 쓰기              | 식별자 검증 후 `repository.deleteByNumber`                      |
| 일부 수정   | `updateStock`     | 쓰기              | 읽고, 엔티티의 변경 메서드(`increaseStock`)를 호출, `update`    |

- 값을 바꾸는 규칙(재고 증감 같은 계산과 그 검증)은 서비스가 아니라 **엔티티의 메서드**에 둠. 서비스는 엔티티 메서드를 호출만 함
- 식별자를 파라미터로 받아서 바로 쓰는 메서드(`getProduct`, `deleteProduct`)는 시작할 때 `Product.validateNumber(number)`를 호출

## 중복 확인

- 생성할 때 `repository.existsByNumber(...)`가 `true`이면 `DomainEntityAlreadyExistsException("Product", number)`를 던짐
- 존재 확인은 `domain`이 모르는 저장된 상태에 의존하므로 서비스의 일

```java
@Transactional
public Product createProduct(Product product) {
    if (productRepository.existsByNumber(product.getNumber())) {
        throw new DomainEntityAlreadyExistsException("Product", product.getNumber());
    }

    return productRepository.create(product);
}
```

## 없는 경우

- 서비스에서 `Optional`을 풀어서 예외를 던지지 않음. `repository.getByXxx`가 없으면 `DomainEntityNotFoundException`을 던지므로 그걸 호출 ([infrastructure](infrastructure.md))

# 정하지 않은 것

> 규칙으로 적지 않음. 임의로 정하지 않음

- 클래스에 붙은 `@Validated`는 파라미터에 제약 애노테이션이 없어서 지금은 효과가 없음. 새 서비스에 붙일지 여부
- 서비스가 커졌을 때 유스케이스 단위로 나누는 기준
- 여러 도메인을 한 트랜잭션에서 다루는 방식
