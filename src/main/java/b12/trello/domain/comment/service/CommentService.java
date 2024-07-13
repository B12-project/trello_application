package b12.trello.domain.comment.service;

import b12.trello.domain.card.entity.Card;
import b12.trello.domain.card.repository.CardRepository;
import b12.trello.domain.comment.dto.CommentRequestDto;
import b12.trello.domain.comment.dto.CommentResponseDto;
import b12.trello.domain.comment.entity.Comment;
import b12.trello.domain.comment.repository.CommentRepository;
import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.customException.CommentException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import b12.trello.global.exception.errorCode.CommentErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto commentRequestDto, User user) {
        Card card = cardRepository.findById(commentRequestDto.getCardId())
                .orElseThrow(() -> new CardException(CardErrorCode.CARD_NOT_FOUND));

        Comment comment = Comment.builder()
                .card(card)
                .user(user)
                .Contents(commentRequestDto.getContent())
                .build();

        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllByCardId(Long cardId) {
        List<Comment> comments = commentRepository.findAllByCardIdOrderByCreatedAtDesc(cardId);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponseDto modifyComment(Long commentId, CommentRequestDto commentRequestDto, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CardException(CardErrorCode.CARD_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.ACCESS_DENIED);
        }

        comment.update(commentRequestDto.getContent());
        return new CommentResponseDto(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CardException(CardErrorCode.CARD_NOT_FOUND));

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }
}
