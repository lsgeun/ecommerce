---
updated: 2026-10-22
---

- `infrastructure` 패키지의 RepositoryAdapter, JpaRepository, JpaCustomRepository 규칙
- 역할은 `domain`의 Repository 인터페이스를 JPA로 구현하는 것뿐이고, 비즈니스 규칙은 두지 않음

# 구성

```
{Domain}Repository (domain, 인터페이스)
    ^ implements
{Domain}RepositoryAdapter (infrastructure)
    -> {Domain}JpaRepository (Spring Data JPA 인터페이스)
           extends JpaRepository<{Domain}, Long>, {Domain}JpaCustomRepository
                       ^ implements
                   {Domain}JpaCustomRepositoryImpl
```

- `application`은 `{Domain}Repository`만 알고, 이 패키지의 클래스를 **절대** 모름

# RepositoryAdapter

- 이름은 `{Domain}RepositoryAdapter`
- 애노테이션은 `@RequiredArgsConstructor`, `@Repository`. `{Domain}Repository`를 `implements`
- 의존성은 `{Domain}JpaRepository`를 `private final` 필드로 둠

## 메서드 구현

| 메서드             | 구현                                                                               |
| ------------------ | ---------------------------------------------------------------------------------- |
| `findByNumber`     | `jpaRepository.findByNumberWithCache(number)` (naturalId 캐시 조회)                |
| `getByNumber`      | `findByNumber(...).orElseThrow(() -> new DomainEntityNotFoundException("Product", number))` |
| `create`, `update` | `jpaRepository.save(...)`                                                          |
| `deleteByNumber`   | `getByNumber`로 읽고 `jpaRepository.delete(...)` 후 지운 엔티티를 반환              |
| `existsByNumber`   | `jpaRepository.existsByNumber(number)`                                             |

- 없는 경우를 알리는 예외 `DomainEntityNotFoundException`을 던지는 곳은 **Adapter의 `getByXxx`** 하나. 첫 번째 인자는 도메인 이름 문자열(`"Product"`), 두 번째 인자는 식별자
- Adapter에 `@Transactional`을 붙이지 않음. 트랜잭션은 `application`의 서비스가 시작

# JpaRepository

- 이름은 `{Domain}JpaRepository`
- `JpaRepository<{Domain}, Long>`와 `{Domain}JpaCustomRepository`를 함께 `extends`
- 메서드는 Spring Data의 이름 규칙으로 만들 수 있는 것만 둠 (`existsByNumber`)

# JpaCustomRepository

- 이름은 인터페이스 `{Domain}JpaCustomRepository`, 구현 `{Domain}JpaCustomRepositoryImpl`
- 구현 이름은 **반드시** 인터페이스 이름 뒤에 `Impl`. Spring Data가 이 이름으로 구현을 찾아서 `JpaRepository`에 연결
- 구현은 `@Component`, `@RequiredArgsConstructor`로 `EntityManager`를 주입

## naturalId 캐시 조회

- 자연키로 조회할 때는 Hibernate의 `byNaturalId`를 써서 1차 캐시를 활용

```java
public Optional<Product> findByNumberWithCache(String number) {
    Product product = entityManager.unwrap(Session.class)
        .byNaturalId(Product.class)
        .using("number", number)
        .load();

    return Optional.ofNullable(product);
}
```

- 이름은 `findBy{필드}WithCache`, 반환은 `Optional`
- 엔티티의 `@NaturalId` 필드 이름과 `using(...)`의 첫 번째 인자가 **반드시** 같아야 함

# 정하지 않은 것

> 규칙으로 적지 않음. 임의로 정하지 않음

- 목록 조회, 페이지네이션, 복합 조건 조회를 구현하는 방식
- 동시 요청으로 유니크 제약을 위반했을 때(`DataIntegrityViolationException`)의 처리 ([exception](exception.md) 참고)
