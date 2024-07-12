package b12.trello.domain.board.entity;

import b12.trello.domain.board.dto.BoardRequestDto;
import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.user.entity.User;
import b12.trello.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Board extends TimeStamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String boardName;

    @Column
    private String boardInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private User manager;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardUser> boardUsers = new ArrayList<>();

    // 빌더 패턴 추가
    @Builder
    public Board(String boardName, String boardInfo, User manager) {
        this.boardName = boardName;
        this.boardInfo = boardInfo;
        this.manager = manager;
    }

    public void update(BoardRequestDto boardRequestDto) {
        this.boardName = boardRequestDto.getBoardName();
        this.boardInfo = boardRequestDto.getBoardInfo();
    }

    public void addUser(BoardUser boardUser) {
        boardUsers.add(boardUser);
        boardUser.setBoard(this);
    }

    public void removeUser(BoardUser boardUser) {
        boardUsers.remove(boardUser);
        boardUser.setBoard(null);
    }
}

