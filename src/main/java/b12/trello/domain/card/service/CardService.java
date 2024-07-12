package b12.trello.domain.card.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import b12.trello.domain.card.dto.request.CardCreateRequestDto;
import b12.trello.domain.card.dto.request.CardListByColumnRequestDto;
import b12.trello.domain.card.dto.request.CardModifyRequestDto;
import b12.trello.domain.card.dto.response.CardListByColumnResponseDto;
import b12.trello.domain.card.dto.response.CardFindResponseDto;
import b12.trello.domain.card.entity.Card;
import b12.trello.domain.card.repository.CardRepository;
import b12.trello.domain.card.repository.CardSearchCond;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.column.repository.ColumnRepository;
import b12.trello.domain.user.entity.User;
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
    private final BoardUserRepository boardUserRepository;
    private final ColumnRepository columnRepository;
    private final CardRepository cardRepository;

    @Transactional
    public void createCard(User user, CardCreateRequestDto requestDto) {
        // 컬럼이 존재하는지 확인
        Columns column = columnRepository.findByIdOrElseThrow(requestDto.getColumnId());
        checkBoardStatusAndBoardUser(user, column);

        User worker = null;

        if (requestDto.getUserId() != null) {
            // 작업자가 해당 보드의 참여자인지 검증
            worker = boardUserRepository.findByBoardIdAndUserIdOrElseThrow(column.getBoard().getId(), requestDto.getUserId()).getUser();
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

    public CardFindResponseDto findCard(User user, Long cardId) {
        Card card = getValidatedCardAndCheckBoardUser(user, cardId);
        return CardFindResponseDto.of(card);
    }

    public CardListByColumnResponseDto findCardListByColumn(User user, CardListByColumnRequestDto requestDto) {
        Columns column = columnRepository.findByIdOrElseThrow(requestDto.getColumnId());
        checkBoardStatusAndBoardUser(user, column);

        return CardListByColumnResponseDto.of(column);
    }

    public CardListByColumnResponseDto searchCardListByColumn(User user, CardListByColumnRequestDto requestDto, String search) {
        Columns column = columnRepository.findByIdOrElseThrow(requestDto.getColumnId());
        checkBoardStatusAndBoardUser(user, column);

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
        Card card = getValidatedCardAndCheckBoardUser(user, cardId);
        Columns column = card.getColumn();

        if (requestDto.getColumnId() != null) {
            column = columnRepository.findByIdOrElseThrow(requestDto.getColumnId());
        }

        // 지정한 컬럼이 현재 보드에 속하는지 검증
        card.validateColumnAndBoard(column);

        // 작업자를 없앨 수도 있기 때문에 null로 설정
        User worker = null;

        if (requestDto.getWorkerId() != null) {
            worker = boardUserRepository.findByBoardIdAndUserIdOrElseThrow(column.getBoard().getId(), requestDto.getWorkerId()).getUser();
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
        Card card = getValidatedCardAndCheckBoardUser(user, cardId);
        cardRepository.delete(card);
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
