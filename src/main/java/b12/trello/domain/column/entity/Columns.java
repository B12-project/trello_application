package b12.trello.domain.column.entity;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.card.entity.Card;
import b12.trello.global.entity.TimeStamped;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@NoArgsConstructor
@Entity
public class Columns extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long columnId;

    @Setter
    @Column(nullable = false)
    private String columnName;

    @Setter
    @Column(nullable = false)
    private Long columnOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    @Builder
    public Columns(String columnName, Board board, Long order){
        this.columnName = columnName;
        this.board = board;
        this.columnOrder = order;
    }


}
