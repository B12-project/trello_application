package b12.trello.domain.card.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.boardUser.service.BoardUserService;
import b12.trello.domain.card.dto.request.CardCreateRequestDto;
import b12.trello.domain.card.dto.request.CardModifyRequestDto;
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
        Columns column = findColumnById(requestDto.getColumnId());

        // 컬럼이 포함된 보드가 삭제된 보드인지 검증
        Board board = column.getBoard();
        board.validateBoardStatus();

        // 요청한 유저가 해당 보드의 참여자인지 검증
//        User requestUser = boardUserService.findBoardUser(boardId, user.getId());
        User worker = null;

        if (requestDto.getUserId() != null) {
            // 작업자가 해당 보드의 참여자인지 검증
            worker = boardUserService.findBoardUser(board.getId(), requestDto.getUserId());
        }

        Card newCard = Card.builder()
                .column(column)
                .cardName(requestDto.getCardName())
                .cardContents(requestDto.getCardContents())
                .deadline(requestDto.getDeadline())
                .worker(worker)
                .build();

        cardRepository.save(newCard);
    }

    @Transactional
    public void modifyCard(Long cardId, CardModifyRequestDto requestDto) {
        // 카드가 존재하는지 확인
        Card card = findCardById(cardId);
        Columns column = card.getColumn();

        if (requestDto.getColumnId() != null) {
            column = findColumnById(requestDto.getColumnId());
        }

        // 지정한 컬럼이 현재 보드에 속하는지
        // 보드는 삭제되지 않은 상태인지 검증
        card.validateColumnAndBoard(column);

        // 요청한 유저가 해당 보드의 참여자인지 검증 (시큐리티 이후)
//        User requestUser = boardUserService.findBoardUser(board.getId(), user.getId());

        User worker = card.getWorker();

        if (requestDto.getUserId() != null) {
            // 작업자가 해당 보드의 참여자인지 검증
            worker = boardUserService.findBoardUser(column.getBoard().getId(), requestDto.getUserId());
        }

        card.updateCard(
                column,
                requestDto.getCardName(),
                requestDto.getCardContents(),
                requestDto.getDeadline(),
                worker
        );
    }

    private Card findCardById(Long cardId){
        return cardRepository.findById(cardId).orElseThrow(() ->
                new CardException(CardErrorCode.CARD_NOT_FOUND));
    }

    private Columns findColumnById(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(() ->
                new CardException(CardErrorCode.COLUMN_NOT_FOUND));

    }
}
