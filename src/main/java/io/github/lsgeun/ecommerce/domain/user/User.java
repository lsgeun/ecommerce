package io.github.lsgeun.ecommerce.domain.user;

import io.github.lsgeun.ecommerce.exception.InvalidDomainFieldException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;
import java.util.regex.Pattern;

@Getter
@Entity
@Table(name = "user_tb")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$");
    private static final Pattern PASSWORD_PATTERN =
        Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$");
    private static final Pattern NICKNAME_PATTERN =
        Pattern.compile("^[가-힣a-zA-Z0-9]{2,10}$");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_email", nullable = false, unique = true)
    private String email;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_nickname", nullable = false, unique = true)
    private String nickname;

    @Builder(access = AccessLevel.PRIVATE)
    private User(Long id, String email, String password, String nickname) {
        validateEmail(email);
        validatePassword(password);
        validateNickname(nickname);

        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User create(String email, String password, String nickname) {
        return User.builder().email(email).password(password).nickname(nickname).build();
    }

    // 1. 이메일 검증
    private void validateEmail(String email) {
        if (Objects.isNull(email)) {
            throw new InvalidDomainFieldException(User.class, "email", email, "이메일은 필수입니다.");
        }
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            throw new InvalidDomainFieldException(User.class, "email", email, "올바른 이메일 형식이 아닙니다.");
        }
    }

    // 2. 비밀번호 검증
    private void validatePassword(String password) {
        if (Objects.isNull(password)) {
            throw new InvalidDomainFieldException(User.class, "password", password, "비밀번호는 필수입니다.");
        }
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new InvalidDomainFieldException(User.class, "password", password, "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다.");
        }
    }

    // 3. 닉네임 검증
    private void validateNickname(String nickname) {
        if (Objects.isNull(nickname)) {
            throw new InvalidDomainFieldException(User.class, "nickname", nickname, "닉네임은 필수입니다.");
        }
        if (!NICKNAME_PATTERN.matcher(nickname).matches()) {
            throw new InvalidDomainFieldException(User.class, "nickname", nickname, "닉네임은 한글, 영문, 숫자만 가능하며 2~10자여야 합니다.");
        }
    }
}
