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

    Boolean existsBoardUserByBoardIdAndUserId(Long boardId, Long userId);

    List<BoardUser> findByBoardId(Long boardId);

    default BoardUser findByBoardIdAndUserIdOrElseThrow(Long boardId, Long userId) {
        return findByBoardIdAndUserId(boardId, userId)
            .orElseThrow(() -> new BoardUserException(BoardUserErrorCode.BOARD_USER_NOT_FOUND));
    }

    default void validateBoardUser(Long boardId, Long userId) {
        if (!existsBoardUserByBoardIdAndUserId(boardId, userId)) {
            throw new BoardUserException(BoardUserErrorCode.BOARD_USER_ONLY);
        }
    }

    default void validateNotBoardUser(Long boardId, Long userId) {
        if (existsBoardUserByBoardIdAndUserId(boardId, userId)) {
            throw new BoardUserException(BoardUserErrorCode.BOARD_USER_DUPLICATED);
        }
    }

    default void validateBoardManager(Board board, User user) {
        BoardUser boardUser = findByBoardIdAndUserIdOrElseThrow(board.getId(), user.getId());
        if (boardUser.getBoardUserRole() != BoardUser.BoardUserRole.MANAGER) {
            throw new BoardUserException(BoardUserErrorCode.BOARD_MANAGER_ONLY);
        }
    }

}
