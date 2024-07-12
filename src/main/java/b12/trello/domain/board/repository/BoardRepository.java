package b12.trello.domain.board.repository;

import b12.trello.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // 추가적인 쿼리 메서드 필요 시 정의
}