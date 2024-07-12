package b12.trello.domain.column.dto;

import b12.trello.domain.column.entity.Columns;
import java.time.LocalDateTime;
import lombok.Getter;

@Getter
public class ColumnFindResponseDto {

    private String columnName;
    private Long columnOrder;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Long columnId;

    public ColumnFindResponseDto(Columns column) {
        this.columnName = column.getColumnName();
        this.columnOrder = column.getColumnOrder();
        this.createdAt = column.getCreatedAt();
        this.modifiedAt = column.getModifiedAt();
        this.columnId = column.getColumnId();
    }
}
