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

    List<BoardUser> findByUser(User user);

    List<BoardUser> findByUserAndBoardUserRole(User user, BoardUser.BoardUserRole boardUserRole);

    Optional<BoardUser> findByBoardIdAndUserId(Long boardId, Long userId);

    boolean existsByBoardAndUserAndBoardUserRole(Board board, User user, BoardUser.BoardUserRole boardUserRole);

    Boolean existsBoardUserByBoardIdAndUserId(Long boardId, Long userId);

    default BoardUser findByBoardIdAndUserIdOrElseThrow(Long boardId, Long userId) {
        return findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new BoardUserException(BoardUserErrorCode.BOARD_USER_NOT_FOUND));
    }

    // 보드 참여자가 아닌 경우
    default void verifyBoardUser(Long boardId, Long userId) {
        if (!existsBoardUserByBoardIdAndUserId(boardId, userId)) {
            throw new BoardUserException(BoardUserErrorCode.BOARD_USER_FORBIDDEN);
        }
    }

    // 이미 보드 참여자인 경우
    default void verifyNotBoardUser(Long boardId, Long userId) {
        if (existsBoardUserByBoardIdAndUserId(boardId, userId)) {
            throw new BoardUserException(BoardUserErrorCode.BOARD_USER_FORBIDDEN);
        }
    }
}
