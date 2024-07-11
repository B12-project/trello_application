package b12.trello.domain.column.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.column.dto.ColumnCreateRequestDto;
import b12.trello.domain.column.dto.ColumnFindResponseDto;
import b12.trello.domain.column.dto.ColumnFindRequestDto;
import b12.trello.domain.column.dto.ColumnModifyRequestDto;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.column.repository.ColumnRepository;
import b12.trello.global.exception.customException.column.BoardNotFoundException;
import b12.trello.global.exception.errorCode.column.ColumnErrorCode;
import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ColumnService {

    private final ColumnRepository columnRepository;
    private final BoardRepository boardRepository;

    public void createColumn(ColumnCreateRequestDto requestDto) {
        Board board = boardRepository.findById(requestDto.getBoardId()).orElseThrow();//
        Long columnOrder = columnRepository.countByBoardId(requestDto.getBoardId());
        Columns columns = Columns.builder().board(board).columnName(requestDto.getColumnName()).order(columnOrder).build();//
        columnRepository.save(columns);
    }


    public List<ColumnFindResponseDto> findColumns(ColumnFindRequestDto requestDto) {

        List<ColumnFindResponseDto> columns = columnRepository.findAllByBoardIdOrderByColumnOrderAsc(requestDto.getBoardId()).stream().map(
            ColumnFindResponseDto::new).toList();

        return columns;

    }

    public void modifyColumn(Long columnId, ColumnModifyRequestDto requestDto) {

        Columns columns = columnRepository.findById(columnId).orElseThrow(); //
        columns.setColumnName(requestDto.getColumnName());
        columnRepository.save(columns);
    }

    @Transactional
    public void deleteColumn(Long columnId) {

        Columns columns = columnRepository.findById(columnId).orElseThrow();//
        columnRepository.deleteById(columnId);
        List<Columns> columnsList = columnRepository.findAllByColumnOrderGreaterThan(columns.getColumnOrder());
        for (Columns column : columnsList) {
            column.setColumnOrder(column.getColumnOrder()-1);
        }
    }


    @Transactional
    public void modifyColumnOrder(Long columnId, Long newOrder) {

        Columns columns = columnRepository.findById(columnId).orElseThrow(); //


        if (Objects.equals(columns.getColumnOrder(), newOrder)){
            return;
        }
        else if(columns.getColumnOrder() > newOrder){
            List<Columns> columnsList = columnRepository.findAllByColumnOrderBetween(newOrder, columns.getColumnOrder());
            for(Columns column : columnsList){
                if(Objects.equals(column.getColumnId(), columns.getColumnId())){
                    columns.setColumnOrder(newOrder);
                    continue;
                }
                column.setColumnOrder(column.getColumnOrder()+1);
            }
            return;

        }
        else{
            List<Columns> columnsList = columnRepository.findAllByColumnOrderBetween(columns.getColumnOrder(),newOrder);
            for(Columns column : columnsList){
                if(column.equals(columns)){
                    columns.setColumnOrder(newOrder);
                    continue;
                }
                column.setColumnOrder(column.getColumnOrder()-1);
            }
            return;
        }

    }
}
