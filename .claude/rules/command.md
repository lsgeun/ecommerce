
> 기타 오류가 잘 나는 실행을 작성해두면 좋음
> rm 명령이나 뭐 그런 것들.

# 테스트 실행

백엔드 테스트는 `backend/spring-core-api`에서 Gradle 태스크로 실행
테스트는 종류별 태그(`unit`, `slice`, `integration`)로 나뉘며, 태스크도 종류별로 분리

- 전체: `./gradlew test` (단위, 슬라이스, 통합 순으로 실행 후 태그 없는 나머지 실행. 앞 단계가 실패하면 뒤 단계는 실행되지 않음)
- 단위: `./gradlew unitTest`
- 슬라이스: `./gradlew sliceTest`
- 통합: `./gradlew integrationTest`

특정 테스트 클래스만 실행할 때는 해당 종류의 태스크에 `--tests`를 붙임. 예: `./gradlew unitTest --tests '*ProductTest'`
`test`에 `--tests`를 붙이면 태그 없는 테스트만 대상이라 "No tests found"로 실패하므로 **절대** 쓰지 않음
Gradle이 up-to-date로 건너뛰면 테스트가 실제로 돌지 않으므로, 결과 확인이 필요하면 `--rerun`을 붙임
