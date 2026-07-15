package io.github.lsgeun.ecommerce.service.user;

import io.github.lsgeun.ecommerce.controller.user.UserDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserSimpleService {

    private final UserDtoMapper userDtoMapper;

    @Transactional(readOnly = true)
    public UserDto.ReadResponse getUser(String nickname) {
        return null;
    }

    @Transactional
    public UserDto.CreateResponse createUser(UserDto.CreateRequest createRequest) {
        return null;
    }

    @Transactional
    public UserDto.UpdateResponse updateUser(UserDto.UpdateRequest updateRequest) {
        return null;
    }

    @Transactional
    public UserDto.DeleteResponse deleteUser(String nickname) {
        return null;
    }
}
