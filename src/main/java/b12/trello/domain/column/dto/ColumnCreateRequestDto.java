package b12.trello.domain.column.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter//test
@Getter
public class ColumnCreateRequestDto {
    @NotNull(message = "컬럼을 생성할 보드 아이디는 필수값입니다.")
    private Long boardId;

    @NotBlank(message = "컬럼 이름은 필수값입니다.")
    private String columnName;
}
