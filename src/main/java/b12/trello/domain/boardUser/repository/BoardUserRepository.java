package b12.trello.domain.boardUser.repository;

import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.global.exception.customException.BoardUserException;
import b12.trello.global.exception.errorCode.BoardUserErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    Optional<BoardUser> findByBoardIdAndUserId(Long boardId, Long userId);

    Boolean existsBoardUserByBoardIdAndUserId(Long boardId, Long userId);

    default BoardUser findByBoardIdAndUserIdOrElseThrow(Long boardId, Long userId) {
        return findByBoardIdAndUserId(boardId, userId).orElseThrow(() -> new BoardUserException(BoardUserErrorCode.BOARD_USER_NOT_FOUND));
    }

    default void validateBoardUser(Long boardId, Long userId) {
        if (!existsBoardUserByBoardIdAndUserId(boardId, userId)) {
            throw new BoardUserException(BoardUserErrorCode.BOARD_USER_FORBIDDEN);
        }
    }

}
