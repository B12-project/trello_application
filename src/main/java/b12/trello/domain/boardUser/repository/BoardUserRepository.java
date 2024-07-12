package b12.trello.domain.boardUser.repository;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.BoardUserException;
import b12.trello.global.exception.errorCode.BoardUserErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    boolean existsByBoardAndUser(Board board, User user);
    boolean existsByUserAndBoardUserRole(User user, BoardUser.BoardUserRole boardUserRole);  // 추가

    List<BoardUser> findByUser(User user);

    List<BoardUser> findByUserAndBoardUserRole(User user, BoardUser.BoardUserRole boardUserRole);

    Optional<BoardUser> findByBoardIdAndUserId(Long boardId, Long userId);

    default BoardUser findByBoardIdAndUserIdOrElseThrow(Long boardId, Long userId) {
        return findByBoardIdAndUserId(boardId, userId)
                .orElseThrow(() -> new BoardUserException(BoardUserErrorCode.BOARD_USER_NOT_FOUND));
    }
}
