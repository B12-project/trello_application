package b12.trello.domain.card.dto.response;

import b12.trello.domain.card.entity.Card;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class CardReadResponseDto {
    private Long columnId;
    private String columnName;
    private String cardName;
    private String cardContents;
    private Long workerId;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CardReadResponseDto of(Card card) {
        return CardReadResponseDto.builder()
                .columnId(card.getColumn().getColumnId())
                .columnName(card.getColumn().getColumnName())
                .cardName(card.getCardName())
                .cardContents(card.getCardContents())
                .workerId(card.getWorker().getId())
                .deadline(card.getDeadline())
                .createdAt(card.getCreatedAt())
                .modifiedAt(card.getModifiedAt())
                .build();
    }

}
