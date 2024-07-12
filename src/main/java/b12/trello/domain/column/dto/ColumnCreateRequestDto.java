package b12.trello.domain.column.dto;

import lombok.Getter;
import lombok.Setter;

@Setter//test
@Getter
public class ColumnCreateRequestDto {

    private Long boardId;
    private String columnName;
}
