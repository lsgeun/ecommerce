---
paths:
  - "backend/**/src/test/**/{integration, product, user}/**/*.java"
---

# 테스트 및 가짜 객체(Mocking) 작성 원칙

너는 테스트 코드를 작성할 때 가짜 객체(Mock/Fake/Stub) 사용을 엄격히 제한하고, 가능한 한 실제 객체와 상태 기반 검증(State-based Testing)을 우선시해야 한다.

## 공통 규칙

아래 원칙은 `@UnitTest`, `@SliceTest`, `@IntegrationTest` 세 가지 테스트 유형 모두에 공통으로 적용된다.

### 1. 객체 대용 우선순위 (Strict Hierarchy)

테스트 대상의 의존성을 채울 때는 아래 순서를 **반드시** 준수하라.
1. **Real Object (실제 객체):** 순수 자바 객체(POJO), 도메인 엔티티, VO, DTO 등은 무조건 실제 객체를 생성하여 사용한다.
2. **Fake (가짜 구현체):** 메모리 기반 저장소(예: ConcurrentHashMap 기반의 InMemoryRepository) 등 인터페이스의 경량화된 실제 동작 구현체를 우선 검토한다.
3. **Mock / Stub (Mockito, @MockBean):** 아래 "공통 Mocking 허용 조건" 또는 테스트 유형별 규칙에 해당하는 불가피한 경우에만 제한적으로 사용한다.

### 2. 공통 Mocking 허용 조건 (전제 조건)

다음 두 가지 경우를 제외하고는 Mockito(`mock()`, `@Mock`, `@MockBean`, `given()`) 사용을 금지한다. (테스트 유형별 추가 허용 조건은 아래 각 섹션을 참고하라.)
- **외부 시스템 경계:** 외부 HTTP API Client, PG사 결제, 메일/SMS 발송 등 제어할 수 없는 외부 네트워크 통신.
- **비결정적(Non-deterministic) 요인:** `Clock`, `LocalDateTime.now()`, Random 값, UUID 생성기 등 실행 시점마다 결과가 바뀌는 요소.

### 3. 작성 가이드라인

- **Mockito 남발 금지:** 단순 데이터 전달용 클래스나 내부 도메인 로직에 Mockito를 사용하지 마라.
- **협력 객체 세팅:** Entity나 VO는 Mocking하지 말고, Test Fixture 메서드(예: `createMember()`)를 만들어 실제 인스턴스를 조립하라.
- **인터페이스 중심:** 외부 API나 비결정적 요인은 인터페이스로 추상화한 뒤, 테스트 시점에만 Stubbing하거나 Fake 구현체를 주입하라.

## 단위 테스트(@UnitTest) 규칙

- Spring 컨텍스트를 전혀 띄우지 않으므로, `@MockBean`은 애초에 사용할 수 없다.
- 의존성은 순수 자바 객체나 Fake 구현체로 조립하는 것을 원칙으로 하며, Mockito(`@Mock`, `mock()`)는 "공통 Mocking 허용 조건"(외부 시스템 경계, 비결정적 요인)에 해당할 때만 예외적으로 사용한다.

## 슬라이스 테스트(@SliceTest) 규칙

- 검증 대상 레이어(예: `@WebMvcTest`의 Controller, `@DataJpaTest`의 Repository)를 제외한 **하위 레이어와의 경계**는 `@MockBean`으로 격리하는 것을 허용한다.
  - 예: `@WebMvcTest`로 Controller를 단독 검증할 때, Service 레이어 이하는 `@MockBean`으로 대체한다.
- 검증 대상 레이어 내부의 협력 객체(Entity, VO 등)는 공통 규칙과 동일하게 Mocking하지 않고 실제 객체로 조립한다.

## 통합 테스트(@IntegrationTest) 규칙

- `@SpringBootTest`로 모든 빈을 실제로 구동하는 것이 목적이므로, `@MockBean`으로 내부 빈을 대체하지 않는다.
- Mockito 사용은 "공통 Mocking 허용 조건"(외부 시스템 경계, 비결정적 요인)으로 엄격히 제한한다.
