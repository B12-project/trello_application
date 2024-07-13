package b12.trello.domain.card.dto.response;

import b12.trello.domain.card.entity.Card;
import b12.trello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class CardOverViewResponseDto {
    private Long cardId;
    private String cardName;
    private Long workerId;
    private LocalDate deadline;

    public static CardOverViewResponseDto of(Card card) {
        User worker = card.getWorker();
        return CardOverViewResponseDto.builder()
                .cardId(card.getId())
                .cardName(card.getCardName())
                .workerId(worker != null ? worker.getId() : null)
                .deadline(card.getDeadline())
                .build();
    }
}
