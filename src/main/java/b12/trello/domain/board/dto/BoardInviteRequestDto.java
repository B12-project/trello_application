package b12.trello.domain.board.dto;


import b12.trello.domain.boardUser.entity.BoardUser;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardInviteRequestDto {

    private Long boardId;
    private String userEmail; // 이메일로 사용자를 초대할 경우

    private BoardUser.BoardUserRole boardUserRole;
}
