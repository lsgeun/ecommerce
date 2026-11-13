---
name: test-code
description: 백엔드 테스트 코드(단위, 슬라이스, 통합)를 작성, 추가, 수정, 보강하거나 테스트 케이스를 설계할 때 사용. 실패하는 테스트를 고칠 때, 기능을 구현하면서 테스트도 함께 작성할 때, 테스트 파일의 이름과 위치를 정할 때도 반드시 이 스킬을 사용한다.
---

# 1. 테스트 유형

| 유형 | 대상 | Spring 컨텍스트 |
|------|------|-----------------|
| 단위 | 순수 로직, 엔티티 등 순수 Java 객체 | 사용하지 않음 |
| 슬라이스 | 특정 레이어만 (Controller, Repository 등) | 해당 레이어만 구동 |
| 통합 | 여러 기능이나 도메인에 걸친 흐름 | 모든 빈 구동 |

# 2. 작성 원칙

- JUnit 5와 AssertJ(`assertThat`)로 작성하고, Mocking은 Mockito를 쓴다.
- `테스트 대상`(메서드, 유즈케이스 등)마다 관련 테스트 메서드를 `@Nested` 클래스로 묶는다. 중첩은 최상위 클래스와 `@Nested` 클래스까지 2단계만 허용한다. `@Nested` 클래스 아래에 위치한 테스트 메서드의 이름은 `테스트 대상`을 제외하고 검증할 내용에 대한 묘사를 한다.
- `@DisplayName`은 의도가 한눈에 보이게 구체적으로 쓴다.
  예: `@DisplayName("성공: 재고가 충분할 때 주문 생성에 성공하고 PENDING 상태를 반환한다")`
- 모든 테스트 메서드는 `// given`, `// when`, `// then` 주석으로 구분한다.
- 테스트끼리 절대 의존하지 않는다. 실행 순서나 결과에 따라 테스트 결과가 달라져서는 안된다.
- 필요한 데이터는 `@BeforeEach`, 헬퍼 메서드, 공급 메서드, 테스트 메서드의 로컬 변수 등으로 직접 만든다.
- DB는 기본적으로 `@Transactional` 롤백으로 격리한다. 롤백이 안 되는 환경(실제 웹 서버를 띄우는 통합 테스트 등)에서는 `@AfterEach`로 직접 정리한다.

# 3. 어노테이션

- 테스트 클래스 상단에 테스트 유형 어노테이션을 **반드시 하나만** 붙인다. 정의 위치: `backend/spring-core-api/src/test/java/io/github/lsgeun/ecommerce/support/annotation/`
- 테스트 메서드에는 유형 어노테이션을 **절대** 붙이지 않는다.
- 그 외 필요한 표준 어노테이션(`@ExtendWith`, `@WebMvcTest`, `@DataJpaTest`, `@SpringBootTest` 등)은 클래스 상단에 자유롭게 추가한다.

# 4. Mocking

기본은 **실제 객체를 쓰는 것**이다. Mock은 아래 경우에만 쓴다.

- 외부 시스템: 외부 API 호출, 결제, 메일/SMS 발송 등
- 실행할 때마다 값이 달라지는 것: 현재 시간, 랜덤 값, UUID

유형별 추가 규칙:

- **단위:** Spring 컨텍스트가 없으므로 `@MockitoBean`은 쓸 수 없다.
- **슬라이스:** 검증 대상 레이어의 아래 레이어는 `@MockitoBean`으로 대체해도 된다. (예: `@WebMvcTest`로 Controller를 검증할 때 Service는 `@MockitoBean`)
- **통합:** 내부 빈을 `@MockitoBean`으로 대체하지 않는다.

엔티티, DTO 같은 객체는 Mock하지 않고, `createMember()` 같은 헬퍼 메서드로 실제 객체를 만들어 쓴다.

# 5. 파일명과 디렉터리

- **단위:** `<대상클래스명>UnitTest.java`, main과 동일한 패키지 경로
  예: `main/.../product/application/ProductSimpleService.java` -> `test/.../product/application/ProductSimpleServiceUnitTest.java`
- **슬라이스:** `<대상클래스명>SliceTest.java`, main과 동일한 패키지 경로
- **통합:**
  - 한 도메인에 속하는 테스트: `<도메인명><내용>IntegrationTest.java`, 해당 도메인 패키지 아래 `integration` 패키지
    예: `test/.../product/integration/ProductCrudIntegrationTest.java`
    - 한 도메인이라도 검증 목적이 다르면 파일을 나눈다. (생성/조회/수정/삭제, 여러 단계 흐름, 복잡한 조회 등)
  - 여러 도메인에 걸친 흐름: `<유스케이스명>IntegrationTest.java`, 최상위 test 패키지 아래 `integration` 패키지
    예: `test/.../integration/CheckoutIntegrationTest.java`

# 6. 케이스 작성 기준

- Getter/Setter, DTO 기본 생성, Spring 자체 동작은 테스트하지 않는다.
- 메서드 하나당 **정상 케이스 1개 이상 + 던질 수 있는 예외 전부**를 쓴다. 예외 원인이 여러 가지면 원인별로 각각 쓴다.
- 아래 상황이 해당 메서드에 실제로 있다면 케이스로 만든다. 없으면 억지로 만들지 않는다.
  - **대상이 없는 경우:** 조회/수정/삭제할 대상이 존재하지 않음
  - **중복/충돌:** 이미 있는 걸 또 생성, 상태가 맞지 않음
  - **경계값:** 허용 범위의 최솟값/최댓값에서 성공하는 케이스와, 그 바로 바깥 값에서 실패하는 케이스
  - **잘못된 입력:** `null`, 빈 문자열, 음수 등
  - **상태 변화:** 반환값뿐 아니라 객체나 DB의 값이 실제로 바뀌었는지
- 정상 케이스는 대표값 1개로 끝내지 말고 경계값에서도 따로 검증한다. (예: 재고 감소는 임의 값 1건 + 재고가 정확히 0이 되는 1건)

# 7. 기타

- 결과값이나 상태(`assertThat`)를 우선 검증하고, "메서드가 호출됐는지"만 보는 `verify()`는 남발하지 않는다.
- 객체 생성이 길어지면 테스트 전용 헬퍼 메서드로 뺀다.
- 테스트에 필요한 데이터만 만든다.
- `Thread.sleep()`은 쓰지 않는다.
