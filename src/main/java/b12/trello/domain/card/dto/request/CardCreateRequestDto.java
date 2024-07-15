package b12.trello.domain.card.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardCreateRequestDto {
    @NotNull(message = "카드를 생성할 컬럼 아이디는 필수값입니다.")
    private Long columnId;

    @NotBlank(message = "카드 이름은 필수값입니다.")
    private String cardName;

    private String cardContents;
    private Long workerId;

//    @Pattern(message = "yyyy-MM-dd 형식으로 작성해주세요.", regexp = "^(19|20)\\d\\d-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])$")
    private String deadline;
}
