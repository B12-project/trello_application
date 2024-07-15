package b12.trello.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
public class PasswordRequestDto {
    @NotBlank(message = "비밀번호 입력은 필수입니다")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W)(?=\\S+$).{8,20}",
            message = "비밀번호는 영문 대,소문자와 숫자, 특수기호가 적어도 1개 이상씩 포함된 8 ~20자의 비밀번호여야 합니다.")
    private String password;

    @NotBlank(message = "새로운 비밀번호 입력은 필수입니다")
    private String newPassword;

}
