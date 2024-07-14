package b12.trello.domain.column.dto;

import b12.trello.domain.card.dto.response.CardCreateResponseDto;
import b12.trello.domain.column.entity.Columns;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ColumnCreateResponseDto {
    private final Long columnId;
    private final String columnName;
    private final Long columnOrder;
    private final Long boardId;

    public static ColumnCreateResponseDto of(Columns column) {
        return ColumnCreateResponseDto.builder()
                .columnId(column.getColumnId())
                .columnName(column.getColumnName())
                .columnOrder(column.getColumnOrder())
                .boardId(column.getBoard().getId())
                .build();
    }
}
