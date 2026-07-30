package io.github.lsgeun.ecommerce.user.infrastructure;

import io.github.lsgeun.ecommerce.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

interface UserJpaRepository extends JpaRepository<User, Long>, UserJpaCustomRepository {

    boolean existsByNickname(String nickname);
}
