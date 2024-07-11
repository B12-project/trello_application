package b12.trello.domain.boardUser.repository;

import b12.trello.domain.boardUser.entity.BoardUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardUserRepository extends JpaRepository<BoardUser, Long> {
    Optional<BoardUser> findByBoardIdAndUserId(Long boardId, Long userId);
}
