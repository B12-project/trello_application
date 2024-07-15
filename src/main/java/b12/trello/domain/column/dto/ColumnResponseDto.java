package b12.trello.domain.column.dto;

import b12.trello.domain.column.entity.Columns;
import lombok.Getter;

@Getter
public class ColumnResponseDto {
    private final Long columnId;
    private final String columnName;
    private final Long columnOrder;
    private final Long boardId;

    public ColumnResponseDto(Columns column) {
        this.columnId = column.getColumnId();
        this.columnName = column.getColumnName();
        this.columnOrder = column.getColumnOrder();
        this.boardId = column.getBoard().getId();
    }
}
