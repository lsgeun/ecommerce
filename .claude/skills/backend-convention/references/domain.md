---
updated: 2026-10-22
---

- `domain` 패키지의 엔티티, 상태 enum, Repository 인터페이스 규칙과 DB 이름 규칙
- 역할은 데이터, 불변식, 상태 변경. Spring MVC나 `application`, `infrastructure`를 모름

# 엔티티

## 클래스

- 이름은 `{Domain}`. JPA 엔티티가 도메인 모델을 겸함
- 애노테이션: `@Entity`, `@NoArgsConstructor(access = AccessLevel.PROTECTED)`, `@Table(...)`, `@Getter`
- **절대** `@Setter`를 쓰지 않음. 값은 생성자와 상태 변경 메서드로만 바꿈
- 생성은 `private` 생성자에 `@Builder`를 붙여서 `Product.builder()`로만 함

## 필드

| 필드        | 규칙                                                                                      |
| ----------- | ----------------------------------------------------------------------------------------- |
| `id`        | `Long`, `@Id`, `@GeneratedValue(strategy = GenerationType.IDENTITY)`. 내부용이라 밖에 노출하지 않음 |
| 자연키      | `@NaturalId`, `nullable = false`. 밖으로 노출하는 식별자 (`Product.number`, `User.nickname`) |
| 상태        | enum은 `@Enumerated(EnumType.STRING)`                                                      |
| 문자열 길이 | `@Column(length = ...)`를 명시                                                             |

- DB의 `id`는 API 경로나 다른 도메인의 참조에 쓰지 않고, 자연키를 씀

## 검증: `validateXxx`

- 필드마다 `public static void validateXxx(값)`를 엔티티에 둠 (`validateNumber`, `validateName`, `validatePrice`, `validateStock`, `validateStatus`)
- 위반하면 `InvalidDomainFieldException(Product.class, "필드", 입력값, "이유")`를 던짐. 이유는 한글 문장으로 쓰고 마침표로 끝냄
- 호출 위치는 **반드시** 두 곳
    - 생성자: 모든 필드의 `validateXxx`
    - 변경 메서드: 바꾸는 필드의 `validateXxx`
- `static`이므로 `application`도 엔티티를 만들기 전에 값만 검증할 때 호출할 수 있음 (`Product.validateNumber(number)`)

```java
@Builder
private Product(Long id, String number, String name, long price, int stock, ProductStatus status) {
    validateNumber(number);
    validateName(name);
    validatePrice(price);
    validateStock(stock);
    validateStatus(status);

    this.id = id;
    ...
}
```

## 상태 변경 메서드

- 값을 바꾸는 일은 엔티티의 `public` 메서드로 만듦
    - `updateFrom(Product product)`: 다른 엔티티의 값으로 바꿈. 자연키는 바꾸지 않음
    - `increaseStock(int count)`, `decreaseStock(int count)`: 계산한 결과를 `validateStock`으로 검증한 뒤에 대입
- 먼저 검증하고, 통과하면 대입. 검증 전에 필드를 **절대** 바꾸지 않음

# 상태 enum

- 이름은 `{Domain}Status`, 값은 영문 대문자 (`SELLING`, `SOLD_OUT`, `STOPPED`)
- 상태 전이 규칙(`SELLING` -> `SOLD_OUT` 등)은 아직 코드에 없음

# Repository 인터페이스

- 이름은 `{Domain}Repository`, 위치는 `domain`. 구현은 `infrastructure`의 Adapter ([infrastructure](infrastructure.md))
- 메서드 이름 규칙

| 이름             | 반환           | 없을 때                              |
| ---------------- | -------------- | ------------------------------------ |
| `findByXxx`      | `Optional<T>`  | 빈 `Optional`                        |
| `getByXxx`       | `T`            | `DomainEntityNotFoundException`      |
| `existsByXxx`    | `boolean`      | `false`                              |
| `create`         | `T`            |                                      |
| `update`         | `T`            |                                      |
| `delete`, `deleteByXxx` | `T` (지운 엔티티) | `deleteByXxx`는 없으면 `DomainEntityNotFoundException` |

- 반환 타입에 `JpaRepository`, `Page` 같은 Spring Data 타입을 쓰지 않음

# DB 이름 규칙

- 테이블 이름은 `{domain}_tb` (`product_tb`, `user_tb`)
- 유니크 제약 이름은 `uk_{domain}_{컬럼}` (`uk_product_number`, `uk_user_email`, `uk_user_nickname`). `@Table(uniqueConstraints = @UniqueConstraint(name = ..., columnNames = ...))`로 명시
- 자연키와 유니크 컬럼은 도메인 접두를 붙임 (`product_number`, `user_nickname`, `user_email`)
- 키 생성은 `IDENTITY` (MySQL의 `AUTO_INCREMENT`)

# 정하지 않은 것

> 규칙으로 적지 않음. 임의로 정하지 않음

- 일반 컬럼의 이름 접두. `Product`는 `name`, `price`, `status`에 접두가 없고 `stock`은 `stock_quantity`로 다르게 부름
- `Money`, `Quantity` 같은 값 객체 도입
- 상태 전이 규칙
- 다른 도메인을 참조하는 필드 (Aggregate 경계)
- 낙관적 락(`@Version`)
