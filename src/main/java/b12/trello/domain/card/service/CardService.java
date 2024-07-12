package b12.trello.domain.card.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.boardUser.service.BoardUserService;
import b12.trello.domain.card.dto.request.CardCreateRequestDto;
import b12.trello.domain.card.dto.request.CardListByColumnRequestDto;
import b12.trello.domain.card.dto.request.CardModifyRequestDto;
import b12.trello.domain.card.dto.response.CardListByColumnResponseDto;
import b12.trello.domain.card.dto.response.CardReadResponseDto;
import b12.trello.domain.card.entity.Card;
import b12.trello.domain.card.repository.CardRepository;
import b12.trello.domain.card.repository.CardSearchCond;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.column.repository.ColumnRepository;
import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static b12.trello.domain.card.repository.CardSearchCond.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {
    private final BoardUserService boardUserService;
    private final ColumnRepository columnRepository;
    private final CardRepository cardRepository;



    @Transactional
    public void createCard(User user, CardCreateRequestDto requestDto) {
        // 컬럼이 존재하는지 확인
        Columns column = validateAndGetColumnById(requestDto.getColumnId());

        // 컬럼이 포함된 보드가 삭제된 보드인지 검증
        Board board = column.getBoard();
        board.validateBoardStatus();

        // 요청한 유저가 해당 보드의 참여자인지 검증
        boardUserService.findBoardUser(board.getId(), user.getId());

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

    public CardReadResponseDto findCard(User user, Long cardId) {
        Card card = validateAndGetCardById(cardId);
        return CardReadResponseDto.of(card);
    }

    public CardListByColumnResponseDto findCardListByColumn(User user, CardListByColumnRequestDto requestDto) {
        Columns column = validateAndGetColumnById(requestDto.getColumnId());
        Board board = column.getBoard();
        board.validateBoardStatus();

        // 요청한 유저가 해당 보드의 참여자인지 검증
        boardUserService.findBoardUser(board.getId(), user.getId());

        return CardListByColumnResponseDto.of(column);
    }

    public CardListByColumnResponseDto searchCardListByColumn(User user, CardListByColumnRequestDto requestDto, String search) {
        Columns column = validateAndGetColumnById(requestDto.getColumnId());
        Board board = column.getBoard();
        board.validateBoardStatus();

        // 요청한 유저가 해당 보드의 참여자인지 검증
        boardUserService.findBoardUser(board.getId(), user.getId());
        CardSearchCond.CardSearchCondBuilder cond = CardSearchCond.builder();

        switch (search != null ? search : COND_NULL) {
            case COND_WORKER_ID:
                cond.workerId(requestDto.getWorkerId());
            case COND_WORKER_EMAIL :
                cond.workerEmail(requestDto.getWorkerEmail());
            default:
                cond.columnId(column.getColumnId());
        }

        List<Card> cardList = cardRepository.searchCards(cond.build());
        return CardListByColumnResponseDto.of(column, cardList);
    }

    @Transactional
    public void modifyCard(User user, Long cardId, CardModifyRequestDto requestDto) {
        // 카드가 존재하는지 확인
        Card card = validateAndGetCardById(cardId);
        card.updateColumn(requestDto.getCardName());
        Columns column = card.getColumn();

        if (requestDto.getColumnId() != null) {
            column = validateAndGetColumnById(requestDto.getColumnId());
        }

        // 지정한 컬럼이 현재 보드에 속하는지
        // 보드는 삭제되지 않은 상태인지 검증
        card.validateColumnAndBoard(column);

        // 요청한 유저가 해당 보드의 참여자인지 검증 (시큐리티 이후)
        boardUserService.findBoardUser(column.getBoard().getId(), user.getId());

        // 작업자를 없앨 수도 있기 때문에 null로 설정
        User worker = null;

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

        cardRepository.save(card);
    }

    @Transactional
    public void deleteCard(User user, Long cardId) {
        Card card = validateAndGetCardById(cardId);
        Board board = card.getColumn().getBoard();
        board.validateBoardStatus();

        boardUserService.findBoardUser(board.getId(), user.getId());

        cardRepository.delete(card);
    }

    private Card validateAndGetCardById(Long cardId){
        return cardRepository.findById(cardId).orElseThrow(() ->
                new CardException(CardErrorCode.CARD_NOT_FOUND));
    }

    private Columns validateAndGetColumnById(Long columnId) {
        return columnRepository.findById(columnId).orElseThrow(() ->
                new CardException(CardErrorCode.COLUMN_NOT_FOUND));
    }
}
