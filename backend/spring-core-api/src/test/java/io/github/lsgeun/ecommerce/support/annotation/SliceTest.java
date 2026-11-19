package io.github.lsgeun.ecommerce.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import io.github.lsgeun.ecommerce.support.container.TestcontainersConfiguration;
import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestConstructor;

// @DataJpaTest가 DB를 임베디드로 교체하지 않고 Testcontainers MySQL을 그대로 쓰게 함
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
// 테스트 클래스 생성자의 파라미터를 @Autowired 없이도 스프링 빈으로 주입해 @RequiredArgsConstructor를 쓸 수 있게 함
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Import(TestcontainersConfiguration.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Tag("slice")
public @interface SliceTest {
}
