package b12.trello.domain.card.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import b12.trello.domain.card.dto.request.CardColumnModifyRequestDto;
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
import b12.trello.domain.user.repository.UserRepository;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

import static b12.trello.domain.card.repository.CardSearchCond.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class CardService {
    private final BoardUserRepository boardUserRepository;
    private final ColumnRepository columnRepository;
    private final CardRepository cardRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createCard(User user, CardCreateRequestDto requestDto) {
        // 컬럼이 존재하는지 확인
        Columns column = columnRepository.findByIdOrElseThrow(requestDto.getColumnId());
        checkBoardStatusAndBoardUser(user, column);

        User worker = null;

        if (requestDto.getUserId() != null) {
            // 작업자가 해당 보드의 참여자인지 검증
            worker = boardUserRepository.findByBoardIdAndUserIdOrElseThrow(column.getBoard().getId(), requestDto.getUserId()).getUser();
            userRepository.verifyUserStatus(worker.getId());
        }

        Card newCard = Card.builder()
                .column(column)
                .cardName(requestDto.getCardName())
                .cardContents(requestDto.getCardContents())
                .deadline(parseToLocalDate(requestDto.getDeadline()))
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
    public CardFindResponseDto modifyCard(User user, Long cardId, CardModifyRequestDto requestDto) {
        Card card = getValidatedCardAndCheckBoardUser(user, cardId);
        Columns column = card.getColumn();

        if (requestDto.getColumnId() != null) {
            column = columnRepository.findByIdOrElseThrow(requestDto.getColumnId());
        }

        card.validateColumnAndBoard(column);

        User worker = null;
        if (requestDto.getWorkerId() != null) {
            worker = boardUserRepository.findByBoardIdAndUserIdOrElseThrow(column.getBoard().getId(), requestDto.getWorkerId()).getUser();
            userRepository.verifyUserStatus(worker.getId());
        }

        card.updateCard(
                column,
                requestDto.getCardName(),
                requestDto.getCardContents(),
                parseToLocalDate(requestDto.getDeadline()),
                worker
        );

        cardRepository.save(card);
        return CardFindResponseDto.of(card);
    }

    @Transactional
    public void modifyCardColumn(User user, Long cardId, CardColumnModifyRequestDto requestDto) {
        Card card = getValidatedCardAndCheckBoardUser(user, cardId);
        Columns column = columnRepository.findByIdOrElseThrow(requestDto.getColumnId());
        card.validateColumnAndBoard(column);
        card.updateCardColumn(column);
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
        boardUserRepository.validateBoardUser(board.getId(), user.getId());
    }

    private LocalDate parseToLocalDate(String deadline) {
        if(deadline == null) {
            return null;
        } else {
            try {
                return LocalDate.parse(deadline, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            } catch (DateTimeParseException e) {
                throw new CardException(CardErrorCode.DATETIME_PARSE_ERROR);
            }
        }
    }
}
