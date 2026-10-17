---
paths:
  - "backend/**/src/test/**/{integration, product, user}/**/*.java"
---

# 테스트 코드 작성

- **Flat한 구조 유지:** `@Nested` 중첩 클래스를 절대 사용하지 않으며, 단일 클래스 내 평평한 메서드 구조로 작성한다.
- **명확한 @DisplayName:** 테스트 의도가 한눈에 드러나도록 `@DisplayName`을 구체적이고 명확하게 작성한다.
  - 예: `@DisplayName("성공: 재고가 충분할 때 주문 생성에 성공하고 PENDING 상태를 반환한다")`
  - 예: `@DisplayName("실패: 재고가 부족하면 OutOfStockException이 발생한다")`
- **BDD 스타일 Given-When-Then:** 모든 테스트 메서드 내부는 `// given`, `// when`, `// then` 주석 블록을 명확히 구분하여 작성한다.
- **독립성 (Self-Contained):**
  - 모든 테스트는 테스트 실행 순서나 다른 테스트의 결과에 절대 의존하지 않고 독립적으로 동작해야 한다.
  - 데이터 준비: 테스트에 필요한 데이터는 해당 테스트 내부, @BeforeEach, 또는 Fixture/Helper 메서드를 통해 직접 구성한다.
  - 상태 격리 및 격리 주체: 테스트가 공유 자원(DB, 파일, Static 상태 등)에 남긴 영향은 다음 테스트에 절대로 영향을 주지 않아야 한다. DB 상태 격리는 기본적으로 @Transactional 롤백을 활용하되, 실제 웹 환경을 띄우는 통합 테스트 등 롤백이 적용되지 않는 특수 환경에서는 @AfterEach나 Cleanup 유틸리티를 통해 명시적으로 정리한다.
