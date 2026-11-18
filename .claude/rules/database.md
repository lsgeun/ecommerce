
# 엔티티 컬럼 이름

테이블이 이미 네임스페이스 역할을 하므로, 컬럼 이름은 기본적으로 테이블 이름을 포함하지 않는 이름으로 작성. 예: `status`, `quantity`
다른 도메인의 값을 참조하거나 복사한 값은 해당 도메인 이름을 접두사로 붙임. 예: `product_name`, `user_id`
같은 테이블에 같은 종류의 값이 여러 개면 구분되는 접두사를 붙임. 예: `order_status`, `payment_status`

# DB 마이그레이션

DB는 MySQL이며, Flyway 마이그레이션(`backend/spring-core-api/src/main/resources/db/migration/V{버전}__{설명}.sql`)은 MySQL 방언으로 작성
엔티티를 추가, 변경하면 마이그레이션도 함께 작성하고, 컬럼 이름은 "엔티티 컬럼 이름" 규칙을 따름
**이미 적용(배포)된 마이그레이션은 절대 수정하지 않고** 새 버전 파일을 추가
컬럼, 테이블 삭제 등 데이터가 사라지는 변경은 작성 전에 사용자에게 확인
enum 컬럼은 MySQL `ENUM` 타입 대신 `VARCHAR(n)`으로 작성 (값 추가 시 마이그레이션이 필요 없도록)

# db 버전 일치

로컬 환경(local 프로파일), 테스트 환경(test 프로파일), 개발 서버 환경(dev 프로파일)의 db 버전은 모두 일치해야 한다.

# db 서버 실행

local은 spring-boot-docker-compose로 db 서버를 실행하고, test는 testcontainers로 db 서버를 실행하고, dev는 docker compose 명령으로 서버에서 직접 컨테이너 안에서 db 서버를 실행한다.
