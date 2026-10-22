---
updated: 2026-10-22
---

- `presentation` 패키지의 RestController, Dto, DtoMapper 규칙
- 역할은 HTTP 입출력, 형식 검증, DTO와 엔티티 변환뿐이고 비즈니스 규칙은 두지 않음

# RestController

## 클래스

- 이름은 `{Domain}RestController`
- 애노테이션은 `@Validated`, `@RequiredArgsConstructor`, `@RequestMapping(path = "/{복수형}")`, `@RestController`
- 의존성은 `{Domain}SimpleService`, `{Domain}DtoMapper`를 `private final` 필드로 두고 생성자 주입은 `@RequiredArgsConstructor`가 만듦
- **반드시** 클래스에 `@Validated`가 있어야 함. `@PathVariable`에 붙인 `@NotNull`, `@Size`는 클래스에 `@Validated`가 있어야 동작하고, 실패하면 `ConstraintViolationException`이 발생

## 경로

- 리소스 이름은 복수형 (`/products`, `/users`)
- 식별자는 DB의 `id`가 아니라 자연키(`number`, `nickname`)를 경로에 사용 (`/products/{number}`)

| 동작        | 매핑                         | 메서드 이름                | 요청 본문                  | 응답 DTO                    |
| ----------- | ---------------------------- | -------------------------- | -------------------------- | --------------------------- |
| 조회        | `GET /products/{number}`     | `getProduct`               | 없음                       | `ProductDto.Read.Response`  |
| 생성        | `POST /products`             | `createProduct`            | `ProductDto.Create.Request` | `ProductDto.Create.Response` |
| 수정        | `PUT /products`              | `updateProduct`            | `ProductDto.Update.Request` | `ProductDto.Update.Response` |
| 삭제        | `DELETE /products/{number}`  | `deleteProduct`            | 없음                       | `ProductDto.Delete.Response` |
| 일부 수정   | `PATCH /products/{number}/stock` | `updateProductStock`   | `ProductDto.Stock.Request`  | `ProductDto.Update.Response` |

- 수정(`PUT`)은 식별자가 경로가 아니라 본문에 있음 (지금 방식)

## 메서드 본문

순서는 항상 아래와 같고, 메서드 안에 다른 로직을 **절대** 넣지 않음

1. 필요하면 DtoMapper로 요청 DTO를 엔티티로 변환
2. SimpleService 호출
3. DtoMapper로 결과 엔티티를 응답 DTO로 변환해서 반환

```java
@PostMapping
public ResponseEntity<ProductDto.Create.Response> createProduct(
    @RequestBody @Valid ProductDto.Create.Request createRequest
) {
    Product product = productDtoMapper.toProduct(createRequest);

    Product createdProduct = productSimpleService.createProduct(product);

    return ResponseEntity.ok(productDtoMapper.toCreateResponse(createdProduct));
}
```

## 응답

- 응답은 **반드시** `ResponseEntity<DTO>`이고 `ResponseEntity.ok(...)`로 만듦. 생성(`POST`)도 200 (지금 방식)
- 요청 본문 파라미터에는 `@RequestBody @Valid`, 경로 파라미터에는 `@PathVariable`과 제약 애노테이션을 붙임
- 제약의 `message`는 한글 문장으로 쓰고 마침표로 끝냄 (`"상품 번호는 필수입니다."`)

# Dto

## 구조

- 도메인마다 인터페이스 `{Domain}Dto` 하나에 모두 모음
- 동작별 중첩 인터페이스 `Read`, `Create`, `Update`, `Delete`를 두고, 그 안에 `Request`, `Response` 클래스를 둠
- 동작에 특화된 요청이 있으면 `Stock`처럼 중첩 인터페이스를 추가 (`ProductDto.Stock.Request`)
- 응답이 없는 동작에는 `Response`를 만들지 않음

## Request

- `@Value` 클래스
- 필드에 Bean Validation 애노테이션으로 **형식**만 검증 (`@NotNull`, `@Size`, `@Min`, `@Positive`)
- 필드 타입이 `int`처럼 기본형이면 `@NotNull`은 효과가 없음 (기본형은 `null`일 수 없고, 값이 빠지면 `0`이 됨). `ProductDto.Stock.Request.count`에는 붙어 있지만 효과 없는 코드이므로 따라 쓰지 않음

## Response

- `@Value`와 `@JsonInclude(JsonInclude.Include.NON_NULL)` 클래스
- 생성자는 `private`이고 `@Builder`를 붙임
- 비밀번호 같은 민감한 값을 필드로 **절대** 만들지 않음 (지금 `User` 응답에는 `password`가 있음. 정하지 않은 것 참고)

```java
@Value
@JsonInclude(JsonInclude.Include.NON_NULL)
class Response {
    String number;
    String name;
    int price;
    int stock;
    ProductStatus status;

    @Builder
    private Response(String number, String name, int price, int stock, ProductStatus status) { ... }
}
```

# DtoMapper

- MapStruct 인터페이스. `@Mapper(componentModel = "spring")`
- 이름은 `{Domain}DtoMapper`, 위치는 `presentation`
- 메서드 이름
    - 요청 DTO -> 엔티티: `toProduct(ProductDto.Create.Request)`. 오버로딩해서 `Update.Request`도 같은 이름
    - 엔티티 -> 응답 DTO: `toReadResponse`, `toCreateResponse`, `toUpdateResponse`, `toDeleteResponse`
- 메서드는 동작별로 주석(`// Read`, `// Create`)으로 묶음
- 엔티티에는 setter가 없고 `private @Builder` 생성자만 있으므로, 매퍼가 엔티티를 만들 때 이 생성자가 호출되고 그 안의 `validateXxx`가 실행됨 ([validation](validation.md))

# 정하지 않은 것

> 규칙으로 적지 않음. 임의로 정하지 않음

- 생성 응답을 201로 바꿀지, 수정 경로에 식별자를 넣을지
- DTO의 `int price`와 엔티티의 `long price`가 다름. 통일 방식
- 민감 정보를 응답에서 빼는 방식
