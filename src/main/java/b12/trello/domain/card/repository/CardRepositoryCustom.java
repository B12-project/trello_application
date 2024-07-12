package b12.trello.domain.card.repository;

import b12.trello.domain.card.entity.Card;

import java.util.List;

public interface CardRepositoryCustom {
    List<Card> searchCards(CardSearchCond cond);
}
