package b12.trello.domain.boardUser.entity;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class BoardUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardUserRole boardUserRole;

    public BoardUser(Board board, User user, BoardUserRole boardUserRole) {
        this.board = board;
        this.user = user;
        this.boardUserRole = boardUserRole;
    }

    public enum BoardUserRole {
        INVITEE,
        MANAGER
    }
}
