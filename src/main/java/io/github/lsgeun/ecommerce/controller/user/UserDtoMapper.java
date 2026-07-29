package io.github.lsgeun.ecommerce.controller.user;

import io.github.lsgeun.ecommerce.domain.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    // Read
    UserDto.ReadResponse toReadResponse(User user);

    // Create
    User toUser(UserDto.CreateRequest createRequest);

    UserDto.CreateResponse toCreateResponse(User user);

    // Update
    User toUser(UserDto.UpdateRequest updateRequest) ;

    UserDto.UpdateResponse toUpdateResponse(User user);

    // Delete
    UserDto.DeleteResponse toDeleteResponse(User user);
}
