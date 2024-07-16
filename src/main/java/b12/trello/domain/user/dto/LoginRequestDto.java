package b12.trello.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class LoginRequestDto {
    private String email;
    private String password;
}
