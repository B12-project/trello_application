package b12.trello.domain.board.dto;

import b12.trello.domain.board.entity.Board;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequestDto {

    // 보드 제목
    @NotBlank(message = "보드 이름은 필수 입력 값입니다.")
    private String boardName;

    // 보드 내용
    private String boardInfo;



}
