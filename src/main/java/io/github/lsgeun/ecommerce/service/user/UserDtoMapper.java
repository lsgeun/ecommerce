package io.github.lsgeun.ecommerce.service.user;

import io.github.lsgeun.ecommerce.controller.user.UserDto;
import io.github.lsgeun.ecommerce.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    // Read
    UserDto.ReadResponse toReadResponse(User user);

    // Create
    default User toUser(UserDto.CreateRequest createRequest) {
        return User.create(
            createRequest.getNickname(),
            createRequest.getPassword(),
            createRequest.getEmail()
        );
    }

    UserDto.CreateResponse toCreateResponse(User user);

    // Update
    UserDto.UpdateResponse toUpdateResponse(User user);

    default User toUser(UserDto.UpdateRequest updateRequest) {
        return User.create(
            updateRequest.getNickname(),
            updateRequest.getPassword(),
            updateRequest.getEmail()
        );
    }

    // Delete
    UserDto.DeleteResponse toDeleteResponse(User user);
}
