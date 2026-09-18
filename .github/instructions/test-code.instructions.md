---
name: "스프링 부트 테스트 코드 작성 컨벤션"
description: "unit, slice, integration, e2e 테스트 코드 생성 및 수정 시 적용되는 규칙"
applyTo: "backend/spring-core-api/src/test/**/{product,user,integration}/**/*Test.java"
---

# Test Code Generation Guidelines

너는 Spring Boot / Java 테스트 코드 작성 전문가다. 테스트 코드를 작성하거나 수정할 때 아래 명시된 컨벤션과 구조를 반드시 엄격히 준수해라.

## 1. 테스트 클래스 네이밍 및 태그(@Tag) 규칙
테스트의 격리 수준과 목적에 따라 클래스명 서픽스와 JUnit 5 `@Tag`를 명확히 매칭한다.

- **단위 테스트 (Unit Test)**
  - 파일명: `*UnitTest.java` 또는 POJO 도메인 `*Test.java`
  - 클래스 상단 annotation: `@UnitTest`
  - 특이사항: Spring Context를 로딩하지 않고 Mockito 또는 pure Java 객체로 검증한다.
- **슬라이스 테스트 (Slice Test)**
  - 파일명: `*SliceTest.java` (예: `ProductControllerSliceTest`)
  - 클래스 상단 annotation: `@SliceTest`, 레이어별 `@WebMvcTest` 또는 `@DataJpaTest`
- **통합 테스트 (Integration Test)**
  - 파일명: `*IntegrationTest.java`
  - 클래스 상단 annotation: `@IntegrationTest`, `@SpringBootTest`
- **E2E 테스트 (E2E Test)**
  - 파일명: `*E2ETest.java`
  - 클래스 상단 annotation: `@E2ETest`

## 2. 테스트 메서드 작성 필수 규칙
- **@DisplayName 규칙:** 
  - 모든 테스트 메서드에는 반드시 `@DisplayName`을 작성한다.
  - 메서드명(e.g., `test1`) 대신 명확한 한글 문장으로 **" 무엇을 검증하는지 / 어떤 상황인지 "** 기술한다.
  - 예시: `@DisplayName("상품 수량이 부족할 경우 OutOfStockException을 던진다")`
- **BDD (Given-When-Then) 구조:**
  - 메서드 본문은 반드시 `// given`, `// when`, `// then` 주석으로 세 단계를 명확히 구획한다.
  - 각 구획 사이에 빈 줄(empty line)을 두어 가독성을 확보한다.

## 3. 검증(Assertion) 및 Mocking 규칙
- Assertions는 JUnit 5 기본 검증 대신 **`org.assertj.core.api.Assertions.assertThat`**을 우선 사용한다.
- 예외 검증 시 `assertThatThrownBy(() -> ...)` 패턴을 사용하고, 예외 메시지나 타입까지 명확히 검증한다.
- Mocking 시 `BDDMockito`(`given(...)`, `willReturn(...)`) 구문을 사용한다.

## 4. 코드 스니펫 템플릿 예시
```java
@Tag("unit")
class ProductUnitTest {

    @Test
    @DisplayName("정상적인 할인율이 적용되면 할인된 가격을 반환한다")
    void calculateDiscount_success() {
        // given
        Product product = new Product("노트북", 100000);
        int discountRate = 10;

        // when
        int discountedPrice = product.applyDiscount(discountRate);

        // then
        assertThat(discountedPrice).isEqualTo(90000);
    }
}
```
