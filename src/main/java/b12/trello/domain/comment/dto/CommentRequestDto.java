package b12.trello.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @NotNull(message = "카드 ID는 필수 입력 값입니다.")
    private Long cardId;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;
}
