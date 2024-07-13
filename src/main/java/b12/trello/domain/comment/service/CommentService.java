package b12.trello.domain.comment.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import b12.trello.domain.card.entity.Card;
import b12.trello.domain.card.repository.CardRepository;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.comment.dto.CommentModifyRequestDto;
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
    private final BoardUserRepository boardUserRepository;

    @Transactional
    public void createComment(CommentRequestDto requestDto, User user) {
        // CardService와 동일한 메서드 필요
        // Service를 주입받아서 메서드 재사용 할 것인지
        Card card = getValidatedCardAndCheckBoardUser(user, requestDto.getCardId());

        Comment comment = Comment.builder()
                .card(card)
                .user(user)
                .Contents(requestDto.getContent())
                .build();

        commentRepository.save(comment);
    }

    // 댓글 전체 조회
    @Transactional(readOnly = true)
    public List<CommentResponseDto> findAllByCardId(User user, Long cardId) {
        getValidatedCardAndCheckBoardUser(user, cardId);
        List<Comment> comments = commentRepository.findAllByCardIdOrderByCreatedAtDesc(cardId);
        return comments.stream()
                .map(CommentResponseDto::new)
                .toList();
    }

    @Transactional
    public void modifyComment(Long commentId, CommentModifyRequestDto requestDto, User user) {
        Comment comment = getValidatedCommentAndCheckBoardUser(user, commentId);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.ACCESS_DENIED);
        }

        comment.update(requestDto.getContent());
    }

    @Transactional
    public void deleteComment(Long commentId, User user) {
        Comment comment = getValidatedCommentAndCheckBoardUser(user, commentId);

        if (!comment.getUser().getId().equals(user.getId())) {
            throw new CommentException(CommentErrorCode.ACCESS_DENIED);
        }

        commentRepository.delete(comment);
    }

    /**
     * 댓글 관련 작업을 위해 검증된 카드 반환
     */
    private Comment getValidatedCommentAndCheckBoardUser(User user, Long commentId) {
        Comment comment = commentRepository.findCommentByIdOrElseThrow(commentId);
        checkBoardStatusAndBoardUser(user, comment.getCard().getColumn());
        return comment;
    }

    /**
     * 카드 관련 작업을 위한 검증 후, 검증된 카드 반환
     */
    private Card getValidatedCardAndCheckBoardUser(User user, Long cardId) {
        Card card = cardRepository.findCardByIdOrElseThrow(cardId);
        checkBoardStatusAndBoardUser(user, card.getColumn());
        return card;
    }

    /**
     * 해당 카드가 속한 보드가 삭제 상태인지, 유저가 해당 보드 사용자인지 검증
     */
    private void checkBoardStatusAndBoardUser(User user, Columns columns) {
        Board board = columns.getBoard();
        board.checkBoardDeleted();
        boardUserRepository.verifyBoardUser(board.getId(), user.getId());
    }
}
