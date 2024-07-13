package b12.trello.domain.comment.repository;

import b12.trello.domain.card.entity.Card;
import b12.trello.domain.comment.entity.Comment;
import b12.trello.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByCardId(Long cardId);

    List<Comment> findByCardOrderByCreatedAtDesc(Card card);
    Optional<Comment> findByIdAndUser(Long id, User user);

    List<Comment> findAllByCardIdOrderByCreatedAtDesc(Long cardId);
}