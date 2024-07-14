package b12.trello.domain.card.dto.response;

import b12.trello.domain.card.entity.Card;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Getter
@Builder
public class CardCreateResponseDto {
    private Long cardId;
    private Long columnId;
    private String cardName;
    private String cardContents;
    private Long workerId;
    private String deadline;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static CardCreateResponseDto of(Card card) {
        return CardCreateResponseDto.builder()
                .cardId(card.getId())
                .columnId(card.getColumn().getColumnId())
                .cardName(card.getCardName())
                .cardContents(card.getCardContents())
                .workerId(card.getWorker() != null ? card.getWorker().getId() : null)
                .deadline(card.getDeadline() != null ? card.getDeadline().format(DATE_FORMATTER) : null)
                .build();
    }
}
