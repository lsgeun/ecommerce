package io.github.lsgeun.ecommerce.user.presentation;

import io.github.lsgeun.ecommerce.user.domain.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserDtoMapper {

    // Read
    UserDto.Read.Response toReadResponse(User user);

    // Create
    User toUser(UserDto.Create.Request createRequest);

    UserDto.Create.Response toCreateResponse(User user);

    // Update
    User toUser(UserDto.Update.Request updateRequest) ;

    UserDto.Update.Response toUpdateResponse(User user);

    // Delete
    UserDto.Delete.Response toDeleteResponse(User user);
}
