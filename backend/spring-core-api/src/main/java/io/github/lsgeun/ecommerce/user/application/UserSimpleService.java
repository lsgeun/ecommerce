package io.github.lsgeun.ecommerce.user.application;

import io.github.lsgeun.ecommerce.user.domain.User;
import io.github.lsgeun.ecommerce.user.domain.UserRepository;
import io.github.lsgeun.ecommerce.global.exception.DomainEntityAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserSimpleService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public User getUser(String nickname) {
        User.validateNickname(nickname);

        return userRepository.getByNickname(nickname);
    }

    @Transactional
    public User createUser(User user) {
        if (userRepository.existsByNickname(user.getNickname())) {
            throw new DomainEntityAlreadyExistsException("User", user.getNickname());
        }

        return userRepository.create(user);
    }

    @Transactional
    public User updateUser(User user) {
        User userToUpdate = userRepository.getByNickname(user.getNickname());

        userToUpdate.updateFrom(user);

        return userRepository.update(userToUpdate);
    }

    @Transactional
    public User deleteUser(String nickname) {
        User.validateNickname(nickname);

        return userRepository.deleteByNickname(nickname);
    }
}
