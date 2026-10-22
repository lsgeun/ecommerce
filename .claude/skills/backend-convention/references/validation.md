---
updated: 2026-10-22
---

- 입력 값이 어디서, 무엇을, 어떻게 검증되는지
- 검증 위치가 3곳이고, 실패 응답은 모두 `400`, `C002`로 같음

# 검증 3곳

| 위치                  | 검증하는 것                                | 방법                                                        | 실패 시 예외                        |
| --------------------- | ------------------------------------------ | ----------------------------------------------------------- | ----------------------------------- |
| RestController, Dto   | 형식. `null`, 길이, 최솟값                  | Request DTO 필드의 Bean Validation, `@RequestBody @Valid`   | `MethodArgumentNotValidException`   |
| RestController        | 경로 변수의 형식                           | `@PathVariable` 파라미터의 제약 애노테이션 + 클래스 `@Validated` | `ConstraintViolationException`      |
| 엔티티                | 값 규칙. 길이 범위, 0 이상, 상태가 비어 있지 않음 | `public static void validateXxx`                            | `InvalidDomainFieldException`       |

- 엔티티의 검증은 생성자와 변경 메서드가 호출하므로, 어떤 경로로 엔티티가 만들어지든 **반드시** 거침 ([domain](domain.md))
- 서비스는 저장된 상태가 필요한 검증(존재, 중복)을 함 ([application](application.md))

# 실패 응답

- 3곳 모두 `400`, `C002`, `INVALID_INPUT_ERROR`, 로그 `WARN`
- `fieldErrorDetails`에 `{ field, reason }`이 들어감 ([exception](exception.md))
    - DTO와 경로 변수 검증은 위반한 필드 전부가 들어감
    - 엔티티 검증은 처음 위반한 필드 1건만 들어감

# 메시지

- `message`(DTO 애노테이션), `reason`(엔티티)은 한글 문장으로 쓰고 마침표로 끝냄 (`"상품 번호는 2자 이상 50자 이하이어야 합니다."`)
- 같은 규칙의 문구는 DTO, 컨트롤러, 엔티티에서 **같은 문장**으로 맞춤

# 새 필드를 추가할 때

1. 엔티티에 `validateXxx`를 만들고 생성자와 변경 메서드에서 호출 ([domain](domain.md))
2. Request DTO의 필드에 같은 규칙의 Bean Validation 애노테이션을 붙임 ([presentation](presentation.md))
3. 경로 변수로 받는 필드면 RestController 파라미터에도 제약 애노테이션을 붙임
4. 테스트는 `test-code` 스킬의 `references/case-coverage.md`를 읽는다

# 정하지 않은 것

> 규칙으로 적지 않음. 임의로 정하지 않음

- 같은 규칙이 지금 여러 곳에 중복되어 있음. 예를 들어 상품 번호 2자 이상 50자 이하가 `ProductDto`(Create, Update), `ProductRestController`의 `@PathVariable`, `Product.validateNumber` 세 곳에 있음. 규칙을 바꾸면 모두 고쳐야 함
- 어느 곳을 규칙의 단일 출처로 할지
