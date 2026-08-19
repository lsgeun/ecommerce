package io.github.lsgeun.ecommerce.user.infrastructure;


import io.github.lsgeun.ecommerce.user.domain.User;

import java.util.Optional;

public interface UserJpaCustomRepository {

    Optional<User> findByNicknameWithCache(String nickname);
}
