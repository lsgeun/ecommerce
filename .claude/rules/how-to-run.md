
# 백엔드 실행

```
./gradlew bootRun
```

# 컴파일 확인

```
./gradlew compileJava compileTestJava
```

- 코드를 고친 뒤 가장 빨리 오류를 확인하는 방법

# 테스트 실행

> [!todo] 설정을 통해서 testAll 대신 test를 쓰면 더 좋을 거 같음

| 명령                          | 실행 대상                       |
| --------------------------- | --------------------------- |
| `./gradlew unitTest`        | `@UnitTest` 태그              |
| `./gradlew sliceTest`       | `@SliceTest` 태그             |
| `./gradlew integrationTest` | `@IntegrationTest` 태그       |
| `./gradlew e2eTest`         | `@E2ETest` 태그               |
| `./gradlew testAll`         | 위 4개를 단위, 슬라이스, 통합, e2e 순으로 |
| `./gradlew test`            | 기본 테스트 코드 실행, 비활성화          |

- 특정 클래스나 메서드만 실행하는 방법, 결과가 캐시될 때 `--rerun`을 붙이는 방법은 `test-code` 스킬의 `references/test-running.md`를 읽는다

# 전체 빌드

```
./gradlew clean build
```
