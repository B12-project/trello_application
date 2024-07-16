package b12.trello.domain.card.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardListByColumnRequestDto {
    @NotNull(message = "조회할 컬럼 아이디는 필수값입니다.")
    private Long columnId;
    private Long workerId;
    private String workerEmail;
}
