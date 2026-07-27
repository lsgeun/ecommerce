package io.github.lsgeun.ecommerce.infrastructure.user;


import io.github.lsgeun.ecommerce.domain.user.User;

import java.util.Optional;

public interface UserJpaCustomRepository {

    Optional<User> findByNicknameWithCache(String nickname);
}
