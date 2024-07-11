package b12.trello.domain.card.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.boardUser.service.BoardUserService;
import b12.trello.domain.card.dto.request.CardCreateRequestDto;
import b12.trello.domain.card.entity.Card;
import b12.trello.domain.card.repository.CardRepository;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.column.repository.ColumnRepository;
import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {
    private final BoardRepository boardRepository;
    private final BoardUserService boardUserService;
    private final ColumnRepository columnRepository;
    private final CardRepository cardRepository;

    @Transactional
    public void createCard(CardCreateRequestDto requestDto) {
        // 컬럼이 존재하는지 확인
        Columns column = columnRepository.findById(requestDto.getColumnId()).orElseThrow(() ->
                new CardException(CardErrorCode.COLUMN_NOT_FOUND));

        // 컬럼이 포함된 보드가 삭제된 보드인지 검증
        Board board = boardRepository.findById(column.getBoard().getId()).orElseThrow(() ->
                new CardException(CardErrorCode.BOARD_STATUS_DELETED));
        log.info("board.getId : {}",board.getId());

        // 요청한 유저가 해당 보드의 참여자인지 검증
//        User requestUser = boardUserService.findBoardUser(boardId, user.getId());

        Card.CardBuilder cardBuilder = Card.builder()
                .column(column)
                .cardName(requestDto.getCardName())
                .cardContents(requestDto.getCardContents() != null ? requestDto.getCardContents() : null)
                .deadline(requestDto.getDeadline() != null ? requestDto.getDeadline() : null);

        if (requestDto.getUserId() != null) {
            // 작업자가 해당 보드의 참여자인지 검증
            User worker = boardUserService.findBoardUser(board.getId(), requestDto.getUserId());
            cardBuilder.worker(worker);
        }

        Card newCard = cardBuilder.build();
        cardRepository.save(newCard);
    }
}
