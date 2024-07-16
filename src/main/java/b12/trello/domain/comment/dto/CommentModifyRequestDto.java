package b12.trello.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentModifyRequestDto {
    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;
}
