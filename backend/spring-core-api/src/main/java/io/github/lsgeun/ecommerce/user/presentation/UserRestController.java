package io.github.lsgeun.ecommerce.user.presentation;

import io.github.lsgeun.ecommerce.user.domain.User;
import io.github.lsgeun.ecommerce.user.application.UserSimpleService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
@RestController
public class UserRestController {

    private final UserSimpleService userSimpleService;
    private final UserDtoMapper userDtoMapper;

    @GetMapping("/{nickname}")
    public ResponseEntity<UserDto.Read.Response> getUser(
        @PathVariable @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 한글, 영문, 숫자만 가능하며 2~10자여야 합니다."
        )
        String nickname
    ) {
        User user = userSimpleService.getUser(nickname);

        return ResponseEntity.ok(userDtoMapper.toReadResponse(user));
    }

    @PostMapping("")
    public ResponseEntity<UserDto.Create.Response> createUser(
        @RequestBody @Valid  UserDto.Create.Request createRequest
    ) {
        User user = userDtoMapper.toUser(createRequest);

        User createdUser = userSimpleService.createUser(user);

        return ResponseEntity.ok(userDtoMapper.toCreateResponse(createdUser));
    }

    @PutMapping("")
    public ResponseEntity<UserDto.Update.Response> updateUser(
        @RequestBody @Valid UserDto.Update.Request updateRequest
    ) {
        User user = userDtoMapper.toUser(updateRequest);

        User updatedUser = userSimpleService.updateUser(user);

        return ResponseEntity.ok(userDtoMapper.toUpdateResponse(updatedUser));
    }

    @DeleteMapping("/{nickname}")
    public ResponseEntity<UserDto.Delete.Response> deleteUser(
        @PathVariable @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 한글, 영문, 숫자만 가능하며 2~10자여야 합니다."
        )
        String nickname
    ) {
        User deletedUser = userSimpleService.deleteUser(nickname);

        return ResponseEntity.ok(userDtoMapper.toDeleteResponse(deletedUser));
    }
}
