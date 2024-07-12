package b12.trello.domain.user.dto;

import b12.trello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignupResponseDto {

    private String name;

    public static SignupResponseDto of(User newUser) {
        return SignupResponseDto.builder().name(newUser.getName()).build();
    }
}
