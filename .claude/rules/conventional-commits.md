
# 공식 문서

[Conventional Commits](https://www.conventionalcommits.org/en/v1.0.0/)

# 커밋 컨벤션

아래 코드는 커밋 작성 구조

```
<type>(<scope>): <short description>

[optional body]

[optional footer(s)]
```

`type`는 변경 사항의 종류 (아래에 설명할 접두사들)
`scope`는 변경 사항이 영향을 미치는 범위 (선택 사항, 예: agent, auth, ui 등)
`short description`는 변경 사항에 대한 짧은 요약

| 접두사 (Type)     | 의미 및 용도                                                      | 예시                                         |
| -------------- | ------------------------------------------------------------ | ------------------------------------------ |
| **`feat`**     | 새로운 **기능(Feature)** 추가 (사용자 입장에서 새 기능이 생겼을 때)                | `feat(auth): 소셜 로그인 기능 추가`                 |
| **`fix`**      | 버그 **수정**                                                    | `fix(api): 토큰 만료 시간 계산 오류 수정`              |
| **`docs`**     | **문서** 수정 (README, 마크다운, 스킬 가이드, 프롬프트 등)                     | `docs(agent): 스킬 작성 가이드라인 보완`              |
| **`style`**    | 코드 포맷팅, 세미콜론 누락, 코드 변경이 없는 경우 (로직 변경 없음)                     | `style(db): 불필요한 공백 및 들여쓰기 정렬`             |
| **`refactor`** | 코드 **리팩토링** (기능의 변화 없이 코드 구조를 개선할 때)                         | `refactor(user): 서비스 계층 중복 로직 메서드 분리`      |
| **`perf`**     | **성능** 개선 (속도, 메모리 최적화 등)                                    | `perf(query): B-Tree 인덱스 추가로 조회 속도 개선`     |
| **`test`**     | **테스트** 코드 추가 또는 기존 테스트 수정                                   | `test(order): 주문 생성 동시성 테스트 작성`            |
| **`chore`**    | 빌드 프로세스, 패키지 매니저 설정, 도구 설정 등 **기타 잡다한 작업**(코드나 문서에 직접 영향 없음) | `chore(deps): Spring Boot 버전 3.2.x로 업그레이드` |
| **`ci`**       | CI/CD 설정 및 스크립트 수정 (GitHub Actions 등)                        | `ci(workflow): AWS EC2 자동 배포 워크플로우 추가`     |
| **`build`**    | 빌드 시스템이나 외부 의존성에 영향을 미치는 변경 (Gradle, Maven 등)                | `build(deps): MySQL 드라이버 의존성 추가`           |
| **`revert`**   | 이전 커밋을 **되돌리기(Rollback)**                                    | `revert: feat(auth): 소셜 로그인 기능 임시 제거`      |
