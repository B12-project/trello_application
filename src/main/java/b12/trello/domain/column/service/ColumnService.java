package b12.trello.domain.column.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.entity.Board.BoardStatus;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.column.dto.ColumnCreateRequestDto;
import b12.trello.domain.column.dto.ColumnFindResponseDto;
import b12.trello.domain.column.dto.ColumnFindRequestDto;
import b12.trello.domain.column.dto.ColumnModifyRequestDto;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.column.repository.ColumnRepository;
import b12.trello.global.exception.customException.column.BoardNotFoundException;
import b12.trello.global.exception.customException.column.ColumnDuplicatedException;
import b12.trello.global.exception.customException.column.ColumnNotFoundException;
import b12.trello.global.exception.customException.column.InvalidOrderException;
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
        Board board = boardRepository.findById(requestDto.getBoardId())
            .orElseThrow(() -> new BoardNotFoundException(
                ColumnErrorCode.BOARD_NOT_FOUND));
        if (board.getBoardStatus() == BoardStatus.DELETED) {
            throw new BoardNotFoundException(ColumnErrorCode.DELETED_BOARD);
        }
        if (columnRepository.existsByColumnNameAndBoardId(requestDto.getColumnName(),
            requestDto.getBoardId())) {
            throw new ColumnDuplicatedException(ColumnErrorCode.COLUMN_ALREADY_REGISTERED_ERROR);
        }

        Long columnOrder = columnRepository.countByBoardId(requestDto.getBoardId());
        Columns columns = Columns.builder().board(board).columnName(requestDto.getColumnName())
            .order(columnOrder).build();
        columnRepository.save(columns);
    }


    public List<ColumnFindResponseDto> findColumns(ColumnFindRequestDto requestDto) {

        Board board = boardRepository.findById(requestDto.getBoardId())
            .orElseThrow(() -> new BoardNotFoundException(
                ColumnErrorCode.BOARD_NOT_FOUND));
        if (board.getBoardStatus() == BoardStatus.DELETED) {
            throw new BoardNotFoundException(ColumnErrorCode.DELETED_BOARD);
        }

        List<ColumnFindResponseDto> columns = columnRepository.findAllByBoardIdOrderByColumnOrderAsc(
            requestDto.getBoardId()).stream().map(
            ColumnFindResponseDto::new).toList();

        return columns;

    }

    public void modifyColumn(Long columnId, ColumnModifyRequestDto requestDto) {
        Columns columns = columnRepository.findById(columnId)
            .orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));
        if (columnRepository.existsByColumnNameAndBoardId(requestDto.getColumnName(),
            columns.getBoard().getId())) {
            throw new ColumnDuplicatedException(ColumnErrorCode.COLUMN_ALREADY_REGISTERED_ERROR);
        }
        columns.setColumnName(requestDto.getColumnName());
        columnRepository.save(columns);
    }

    @Transactional
    public void deleteColumn(Long columnId) {

        Columns columns = columnRepository.findById(columnId)
            .orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));
        columnRepository.deleteById(columnId);
        List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderGreaterThan(
            columns.getBoard().getId(), columns.getColumnOrder());
        for (Columns column : columnsList) {
            column.setColumnOrder(column.getColumnOrder() - 1);
        }
    }


    @Transactional
    public void modifyColumnOrder(Long columnId, Long newOrder) {

        Columns columns = columnRepository.findById(columnId)
            .orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));

        Long boardId = columns.getBoard().getId();
        Long maxOrder = columnRepository.countByBoardId(boardId) - 1;

        if (newOrder < 0 || newOrder > maxOrder) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        }

        if (Objects.equals(columns.getColumnOrder(), newOrder)) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        } else if (columns.getColumnOrder() > newOrder) {
            List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderBetween(
                boardId, newOrder, columns.getColumnOrder());
            for (Columns column : columnsList) {
                if (Objects.equals(column.getColumnId(), columns.getColumnId())) {
                    columns.setColumnOrder(newOrder);
                    continue;
                }
                column.setColumnOrder(column.getColumnOrder() + 1);
            }
        } else {
            List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderBetween(
                boardId, columns.getColumnOrder(), newOrder);
            for (Columns column : columnsList) {
                if (column.equals(columns)) {
                    columns.setColumnOrder(newOrder);
                    continue;
                }
                column.setColumnOrder(column.getColumnOrder() - 1);
            }
        }

    }
}
