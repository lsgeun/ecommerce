package io.github.lsgeun.ecommerce.support.container;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

// 테스트 실행에서 함께 쓰는 MySQL 컨테이너 정의
// @ServiceConnection이 컨테이너의 접속 정보를 datasource 설정으로 자동 연결한다
@TestConfiguration(proxyBeanMethods = false)
public class TestcontainersConfiguration {

    // dev 서버의 MySQL 버전과 맞춰야 방언 차이로 인한 오류를 미리 잡을 수 있다
    private static final DockerImageName MYSQL_IMAGE = DockerImageName.parse("mysql:8.0");

    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        return new MySQLContainer<>(MYSQL_IMAGE);
    }
}
