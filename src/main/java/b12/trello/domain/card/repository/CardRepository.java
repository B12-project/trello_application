package b12.trello.domain.card.repository;

import b12.trello.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long>,  CardRepositoryCustom{

}
