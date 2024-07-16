package b12.trello.domain.card.repository;

import b12.trello.domain.card.entity.Card;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>,  CardRepositoryCustom {

    default Card findCardByIdOrElseThrow(Long cardId){
        return findById(cardId).orElseThrow(() ->
                new CardException(CardErrorCode.CARD_NOT_FOUND));
    }

}
