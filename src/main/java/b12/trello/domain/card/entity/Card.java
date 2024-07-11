package b12.trello.domain.card.entity;

import b12.trello.domain.card.dto.request.CardCreateRequestDto;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.user.entity.User;
import b12.trello.global.entity.TimeStamped;
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

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "column_id")
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

//    public static Card saveCard(CardCreateRequestDto requestDto) {
//        return Card.builder()
//                .cardName(requestDto.getCardName())
//                .cardContents(requestDto.getCardContents() != null ? requestDto.getCardContents() : null)
//    }
}
