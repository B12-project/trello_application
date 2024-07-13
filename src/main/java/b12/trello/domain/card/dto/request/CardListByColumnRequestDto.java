package b12.trello.domain.card.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardListByColumnRequestDto {
    @NotNull
    private Long columnId;
    private Long workerId;
    private String workerEmail;
}
