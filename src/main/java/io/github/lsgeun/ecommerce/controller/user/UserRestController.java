package io.github.lsgeun.ecommerce.controller.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Validated
@RequestMapping("/api/v1/users")
public class UserRestController {

    @GetMapping("/{nickname}")
    public ResponseEntity<UserDto.ReadResponse> getUser(
        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 한글, 영문, 숫자만 가능하며 2~10자여야 합니다."
        )
        @PathVariable
        String nickname
    ) {
        return ResponseEntity.ok().build();
    }

    @PostMapping("")
    public ResponseEntity<UserDto.CreateResponse> createUser(
        @Valid
        @RequestBody
        UserDto.CreateRequest createRequest
    ) {
        return ResponseEntity.ok().build();
    }

    @PutMapping("")
    public ResponseEntity<UserDto.UpdateResponse> updateUser(
        @Valid
        @RequestBody
        UserDto.UpdateRequest updateRequest
    ) {
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{nickname}")
    public ResponseEntity<UserDto.DeleteResponse> deleteUser(
        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 한글, 영문, 숫자만 가능하며 2~10자여야 합니다."
        )
        @PathVariable
        String nickname
    ) {
        return ResponseEntity.ok().build();
    }
}
