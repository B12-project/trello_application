package b12.trello.domain.card.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CardColumnModifyRequestDto {
    @NotNull(message = "이동할 컬럼 아이디는 필수값입니다.")
    private Long columnId;
}
