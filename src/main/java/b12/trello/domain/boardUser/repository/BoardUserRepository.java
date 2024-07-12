package b12.trello.domain.boardUser.repository;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.boardUser.entity.BoardUser.BoardUserRole;
import b12.trello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {

    boolean existsByBoardAndUserAndBoardUserRole(Board board, User user, BoardUserRole boardUserRole);
}
