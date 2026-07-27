package io.github.lsgeun.ecommerce.infrastructure.user;

import io.github.lsgeun.ecommerce.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserJpaCustomRepositoryImpl implements UserJpaCustomRepository {

    private final EntityManager entityManager;

    @Override
    public Optional<User> findByNicknameWithCache(String nickname) {
        User user = entityManager.unwrap(Session.class)
            .byNaturalId(User.class)
            .using(
                "nickname",
                nickname)
            .load();
        return Optional.ofNullable(user);
    }
}
