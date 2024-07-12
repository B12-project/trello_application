package b12.trello.domain.card.dto.response;

import b12.trello.domain.card.entity.Card;
import b12.trello.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class CardFindResponseDto {
    private Long columnId;
    private String columnName;
    private String cardName;
    private String cardContents;
    private Long workerId;
    private LocalDate deadline;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public static CardFindResponseDto of(Card card) {
        User worker = card.getWorker();
        return CardFindResponseDto.builder()
                .columnId(card.getColumn().getColumnId())
                .columnName(card.getColumn().getColumnName())
                .cardName(card.getCardName())
                .cardContents(card.getCardContents())
                .workerId(worker != null ? worker.getId() : null)
                .deadline(card.getDeadline())
                .createdAt(card.getCreatedAt())
                .modifiedAt(card.getModifiedAt())
                .build();
    }
}
