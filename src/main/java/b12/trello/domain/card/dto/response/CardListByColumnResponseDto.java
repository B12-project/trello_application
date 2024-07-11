package b12.trello.domain.card.dto.response;

import b12.trello.domain.column.entity.Columns;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CardListByColumnResponseDto {
    private Long columnId;
    private String columnName;
    private List<CardOverViewResponseDto> cardList;

    public static CardListByColumnResponseDto of(Columns column) {
        return CardListByColumnResponseDto.builder()
                .columnId(column.getColumnId())
                .columnName(column.getColumnName())
                .cardList(column.getCards().stream().map(CardOverViewResponseDto::of).toList())
                .build();
    }
}
