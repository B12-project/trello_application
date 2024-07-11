package b12.trello.domain.card.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardCreateRequestDto {
    @NotNull(message = "카드를 생성할 컬럼 아이디는 필수값입니다.")
    private Long columnId;

    @NotBlank(message = "카드 이름은 필수값입니다.")
    private String cardName;

    private String cardContents;
    private Long userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deadline;
}
