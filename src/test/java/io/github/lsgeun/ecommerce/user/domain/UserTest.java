package io.github.lsgeun.ecommerce.user.domain;

import io.github.lsgeun.ecommerce.global.exception.InvalidDomainFieldException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserTest {

    private User validUser() {
        return User.builder()
            .nickname("user01")
            .password("Pass123!")
            .email("test@example.com")
            .build();
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNickname")
    @DisplayName("닉네임 형식이 맞지 않으면 예외가 발생한다")
    void validateNickname_형식_불일치(String invalidNickname) {
        assertThatThrownBy(() -> User.validateNickname(invalidNickname))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"short1", "NoSpecial1", "pass"})
    @DisplayName("비밀번호 규칙에 맞지 않으면 예외가 발생한다")
    void validatePassword_규칙_불일치(String invalidPassword) {
        assertThatThrownBy(() -> User.validatePassword(invalidPassword))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @ParameterizedTest
    @ValueSource(strings = {"invalid", "test@", "test@.com"})
    @DisplayName("이메일 형식이 맞지 않으면 예외가 발생한다")
    void validateEmail_형식_불일치(String invalidEmail) {
        assertThatThrownBy(() -> User.validateEmail(invalidEmail))
            .isInstanceOf(InvalidDomainFieldException.class);
    }

    @Test
    @DisplayName("updateFrom으로 이메일만 변경할 수 있다")
    void updateFrom_이메일_변경() {
        User user = validUser();
        User updateSource = User.builder()
            .nickname("user01")
            .password("Pass123!")
            .email("new@example.com")
            .build();

        user.updateFrom(updateSource);

        assertThat(user.getEmail()).isEqualTo("new@example.com");
        assertThat(user.getNickname()).isEqualTo("user01"); // 닉네임은 그대로
    }

    // 테스트에 주입할 파라미터 공급 메서드 (static 필수)
    private static Stream<String> provideInvalidNickname() {
        return Stream.of(
            "a",
            "가".repeat(11),
            "user@!",
            "한글닉네임!"
        );
    }
}