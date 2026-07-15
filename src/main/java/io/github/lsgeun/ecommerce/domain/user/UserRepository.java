package io.github.lsgeun.ecommerce.domain.user;

import java.util.Optional;

public interface UserRepository {

    Optional<User> findByNickname(String number);

    User getByNickname(String number);

    User create(User user);

    User update(User user);

    User delete(User user);

    User deleteByNickname(String number);

    boolean existsByNickname(String number);
}
