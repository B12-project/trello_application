package b12.trello.domain.column.entity;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.card.entity.Card;
import b12.trello.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Columns extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long columnId;

    @Column(nullable = false)
    private String columnName;

    @Column(nullable = false)
    private Long columnOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @OneToMany(mappedBy = "column", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards = new ArrayList<>();

    @Builder
    public Columns(String columnName, Long columnOrder, Board board){
        this.columnName = columnName;
        this.columnOrder = columnOrder;
        this.board = board;
    }



}
