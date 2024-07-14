package b12.trello.domain.board.entity;

import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.user.entity.User;
import b12.trello.global.entity.TimeStampedWithDeletedAt;
import b12.trello.global.exception.customException.BoardException;
import b12.trello.global.exception.errorCode.BoardErrorCode;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE board SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class Board extends TimeStampedWithDeletedAt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String boardName;

    @Column
    private String boardInfo;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardUser> boardUsers = new ArrayList<>();

    // 빌더 패턴 추가
    @Builder
    public Board(String boardName, String boardInfo) {
        this.boardName = boardName;
        this.boardInfo = boardInfo;
    }

    public void updateBoard(String boardName, String boardInfo) {
        this.boardName = boardName;
        this.boardInfo = boardInfo;
    }

    public void addUser(BoardUser boardUser) {
        boardUsers.add(boardUser);
        boardUser.setBoard(this);
    }

    public void removeUser(BoardUser boardUser) {
        boardUsers.remove(boardUser);
        boardUser.setBoard(null);
        // 위 줄은 삭제하지 않습니다. BoardUser 엔티티의 관계를 Board 엔티티와의 연결을 해제하는 중요한 단계
    }

    public void checkBoardDeleted() {
        if (this.getDeletedAt() == null) {
            return;
        }
        throw new BoardException(BoardErrorCode.DELETED_BOARD);
    }
}
