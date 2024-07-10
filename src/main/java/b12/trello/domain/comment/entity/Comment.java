package b12.trello.domain.comment.entity;

import b12.trello.domain.card.entity.Card;
import b12.trello.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import b12.trello.global.entity.TimeStamped;

@Entity
@Getter
@NoArgsConstructor
@Table
public class Comment extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String commentContents;
}
