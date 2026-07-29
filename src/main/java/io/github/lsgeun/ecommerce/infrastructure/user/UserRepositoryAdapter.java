package io.github.lsgeun.ecommerce.infrastructure.user;

import io.github.lsgeun.ecommerce.domain.user.User;
import io.github.lsgeun.ecommerce.domain.user.UserRepository;
import io.github.lsgeun.ecommerce.exception.DomainEntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserRepositoryAdapter implements UserRepository {

    private final UserJpaRepository userJpaRepository;


    @Override
    public Optional<User> findByNickname(String nickname) {
        return userJpaRepository.findByNicknameWithCache(nickname);
    }

    @Override
    public User getByNickname(String nickname) {
        return this.findByNickname(nickname)
            .orElseThrow(() -> new DomainEntityNotFoundException("User", nickname));
    }

    @Override
    public User create(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public User update(User user) {
        return userJpaRepository.save(user);
    }

    @Override
    public User delete(User user) {
        return this.deleteByNickname(user.getNickname());
    }

    @Override
    public User deleteByNickname(String nickname) {
        User user = getByNickname(nickname);

        userJpaRepository.delete(user);

        return user;
    }

    @Override
    public boolean existsByNickname(String nickname) {
        return userJpaRepository.existsByNickname(nickname);
    }
}
