package b12.trello.domain.board.repository;

import b12.trello.domain.board.entity.Board;
import b12.trello.global.exception.customException.BoardException;
import b12.trello.global.exception.errorCode.BoardErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    default Board findByIdOrElseThrow(Long boardId) {
        return findById(boardId).orElseThrow(() ->
                new BoardException(BoardErrorCode.BOARD_NOT_FOUND));
    }
}