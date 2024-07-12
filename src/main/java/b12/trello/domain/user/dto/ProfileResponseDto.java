package b12.trello.domain.user.dto;

import b12.trello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ProfileResponseDto {

    private String email;
    private String name;

    public static ProfileResponseDto of(User newUser) {
        return ProfileResponseDto.builder().name(newUser.getName()).email(newUser.getEmail()).build();
    }
}
