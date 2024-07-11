package b12.trello.domain.card.entity;

import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.user.entity.User;
import b12.trello.global.entity.TimeStamped;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Card extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cardName;

    private String cardContents;

    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "column_id")
    @JsonIgnore
    private Columns column;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User worker;

    @Builder
    public Card(String cardName, String cardContents, LocalDate deadline, Columns column, User worker) {
        this.cardName = cardName;
        this.cardContents = cardContents;
        this.deadline = deadline;
        this.column = column;
        this.worker = worker;
    }

    public void updateCard(Columns column, String cardName, String cardContents, LocalDate deadline, User worker) {
        this.column = column;
        this.cardName = cardName;
        this.cardContents = cardContents;
        this.deadline = deadline;
        this.worker = worker;
    }

//    public void updateCardColumn(Columns column) {
//        validateColumnAndBoard(column);
//        this.column = column;
//    }

    public void validateColumnAndBoard(Columns column) {
        if (this.column.getBoard() != column.getBoard()) {
            throw new CardException(CardErrorCode.INVALID_COLUMN_UPDATE);
        }
        this.column.getBoard().validateBoardStatus();
    }

    public void updateColumn(String cardName) {
        this.cardName = cardName;
    }
}
