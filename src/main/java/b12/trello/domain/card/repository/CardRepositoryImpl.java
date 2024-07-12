package b12.trello.domain.card.repository;

import b12.trello.domain.card.entity.Card;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import static b12.trello.domain.board.entity.QBoard.board;
import static b12.trello.domain.column.entity.QColumns.columns;
import static b12.trello.domain.card.entity.QCard.card;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
public class CardRepositoryImpl implements CardRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Card> searchCards(CardSearchCond cond) {
        return jpaQueryFactory.selectFrom(card)
                .leftJoin(card.column, columns)
                .leftJoin(card.column.board, board)
                .where(
                        columnIdEq(cond.getColumnId()),
                        workerIdEq(cond.getWorkerId()),
                        workerEmailEq(cond.getWorkerEmail())
                )
                .fetch();
    }

    @Override
    public Card findCardById(Long cardId) {
        Card findCard = jpaQueryFactory.selectFrom(card)
                .leftJoin(card.column, columns)
                .fetchJoin()
                .leftJoin(card.column.board, board)
                .fetchJoin()
                .where(card.id.eq(cardId))
                .fetchOne();

        if (findCard == null) {
            throw new CardException(CardErrorCode.CARD_NOT_FOUND);
        }

        return findCard;
    }

    private BooleanExpression columnIdEq(Long columnId) {
        return Objects.nonNull(columnId) ? card.column.columnId.eq(columnId) : null;
    }

    private BooleanExpression workerIdEq(Long workerId) {
        return Objects.nonNull(workerId) ? card.worker.id.eq(workerId) : null;
    }

    private BooleanExpression workerEmailEq(String workerEmail) {
        return Objects.nonNull(workerEmail) ? card.worker.email.eq(workerEmail) : null;
    }
}
