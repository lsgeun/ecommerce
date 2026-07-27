package io.github.lsgeun.ecommerce.controller.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Value;

public interface UserDto {

    @Schema(name = "UserReadResponse")
    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class ReadResponse {

        String nickname;
        String password;
        String email;

        @Builder
        private ReadResponse(String nickname, String password, String email) {
            this.nickname = nickname;
            this.password = password;
            this.email = email;
        }
    }

    @Schema(name = "UserCreateRequest")
    @Value
    class CreateRequest {

        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 한글, 영문, 숫자만 가능하며 2~10자여야 합니다."
        )
        String nickname;

        @NotNull(message = "비밀번호는 필수입니다.")
        @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다."
        )
        String password;

        @NotNull(message = "이메일은 필수입니다.")
        @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "올바른 이메일 형식이 아닙니다."
        )
        String email;
    }

    @Schema(name = "UserCreateResponse")
    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class CreateResponse {

        String nickname;
        String password;
        String email;

        @Builder
        private CreateResponse(String nickname, String password, String email) {
            this.nickname = nickname;
            this.password = password;
            this.email = email;
        }
    }

    @Schema(name = "UserUpdateRequest")
    @Value
    class UpdateRequest {

        @NotNull(message = "닉네임은 필수입니다.")
        @Pattern(
            regexp = "^[가-힣a-zA-Z0-9]{2,10}$",
            message = "닉네임은 한글, 영문, 숫자만 가능하며 2~10자여야 합니다."
        )
        String nickname;

        @NotNull(message = "비밀번호는 필수입니다.")
        @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,20}$",
            message = "비밀번호는 영문, 숫자, 특수문자를 포함하여 8~20자여야 합니다."
        )
        String password;

        @NotNull(message = "이메일은 필수입니다.")
        @Pattern(
            regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "올바른 이메일 형식이 아닙니다."
        )
        String email;
    }

    @Schema(name = "UserUpdateResponse")
    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class UpdateResponse {

        String nickname;
        String password;
        String email;

        @Builder
        private UpdateResponse(String nickname, String password, String email) {
            this.nickname = nickname;
            this.password = password;
            this.email = email;
        }
    }

    @Schema(name = "UserDeleteResponse")
    @Value
    @JsonInclude(JsonInclude.Include.NON_NULL)
    class DeleteResponse {

        String nickname;
        String password;
        String email;

        @Builder
        private DeleteResponse(String nickname, String password, String email) {
            this.nickname = nickname;
            this.password = password;
            this.email = email;
        }
    }
}
