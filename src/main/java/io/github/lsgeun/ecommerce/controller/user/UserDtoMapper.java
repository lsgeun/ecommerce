package io.github.lsgeun.ecommerce.controller.user;

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
    default User toUser(UserDto.UpdateRequest updateRequest) {
        return User.create(
            updateRequest.getNickname(),
            updateRequest.getPassword(),
            updateRequest.getEmail()
        );
    }

    UserDto.UpdateResponse toUpdateResponse(User user);

    // Delete
    UserDto.DeleteResponse toDeleteResponse(User user);
}
