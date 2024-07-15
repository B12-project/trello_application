package b12.trello.domain.board.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardUserResponseDto {
    private final Long userId;
    private final String email;
    private final String userName;

    public BoardUserResponseDto(Long userId, String email, String userName) {
        this.userId = userId;
        this.email = email;
        this.userName = userName;
    }
}
