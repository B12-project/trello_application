package b12.trello.domain.board.entity;

import b12.trello.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Board extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String boardName;

    @Column
    private String boardInfo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BoardStatus boardStatus;

    public enum BoardStatus {
        ACTIVE,
        DELETED
    }

    @Builder
    public Board(String boardName, String boardInfo, BoardStatus boardStatus) {
        this.boardName = boardName;
        this.boardInfo = boardInfo;
        this.boardStatus = boardStatus;
    }
}
