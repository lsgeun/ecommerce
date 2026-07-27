package io.github.lsgeun.ecommerce.infrastructure.user;

import io.github.lsgeun.ecommerce.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserJpaRepository extends JpaRepository<User, Long>, UserJpaCustomRepository {

    boolean existsByNickname(String nickname);
}
