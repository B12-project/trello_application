package b12.trello.domain.column.service;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import b12.trello.domain.column.dto.ColumnCreateRequestDto;
import b12.trello.domain.column.dto.ColumnFindResponseDto;
import b12.trello.domain.column.dto.ColumnFindRequestDto;
import b12.trello.domain.column.dto.ColumnModifyRequestDto;
import b12.trello.domain.column.dto.ColumnOrderModifyRequestDto;
import b12.trello.domain.column.entity.Columns;
import b12.trello.domain.column.repository.ColumnRepository;
import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.column.InvalidOrderException;
import b12.trello.global.exception.customException.column.InvalidUserException;
import b12.trello.global.exception.errorCode.ColumnErrorCode;
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
    private final BoardUserRepository boardUserRepository;


    //컬럼생성
    public void createColumn(User user, ColumnCreateRequestDto requestDto) {

        Board board = checkBoard(requestDto.getBoardId());

        validateManager(board, user);

        columnRepository.existsByColumnNameAndBoardIdOrElseThrow(requestDto.getColumnName(), requestDto.getBoardId());

        Long columnOrder = columnRepository.countByBoardId(requestDto.getBoardId()); //컬럼 마지막 순서 계산

        Columns columns = Columns.builder()
            .board(board)
            .columnName(requestDto.getColumnName())
            .order(columnOrder)
            .build(); //컬럼 생성

        columnRepository.save(columns);
    }

    //컬럼 조회
    public List<ColumnFindResponseDto> findColumns(ColumnFindRequestDto requestDto) {

        checkBoard(requestDto.getBoardId());


        List<ColumnFindResponseDto> columns = columnRepository.findAllByBoardIdOrderByColumnOrderAsc(
            requestDto.getBoardId()).stream().map(ColumnFindResponseDto::new).toList(); //순서 오름차순으로 컬럼조회

        return columns;
    }

    //컬럼 수정
    public void modifyColumn(User user, Long columnId, ColumnModifyRequestDto requestDto) {

        Columns columns = columnRepository.findByIdOrElseThrow(columnId);

        validateManager(columns.getBoard(), user);

        columnRepository.existsByColumnNameAndBoardIdOrElseThrow(requestDto.getColumnName(), columns.getBoard().getId());

        columns.setColumnName(requestDto.getColumnName());

        columnRepository.save(columns);
    }

    //컬럼 삭제
    @Transactional
    public void deleteColumn(User user, Long columnId) {

        Columns columns = columnRepository.findByIdOrElseThrow(columnId);

        validateManager(columns.getBoard(), user);

        columnRepository.deleteById(columnId); //컬럼 삭제

        //컬럼 순서 조정
        List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderGreaterThan(
            columns.getBoard().getId(), columns.getColumnOrder());
        for (Columns column : columnsList) {
            column.setColumnOrder(column.getColumnOrder() - 1);
        }
    }


    @Transactional
    public void modifyColumnOrder(User user, Long columnId, ColumnOrderModifyRequestDto requestDto) {

        Long newOrder = requestDto.getOrderId();

        Columns columns = columnRepository.findByIdOrElseThrow(columnId);

        validateManager(columns.getBoard(), user);

        Long boardId = columns.getBoard().getId();

        // valid 순서인지 확인
        Long maxOrder = columnRepository.countByBoardId(boardId) - 1;
        if (newOrder < 0 || newOrder > maxOrder) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        }

        // 순서 변경 로직
        if (Objects.equals(columns.getColumnOrder(), newOrder)) {
            throw new InvalidOrderException(ColumnErrorCode.INVALID_ORDER);
        } else if (columns.getColumnOrder() > newOrder) {
            List<Columns> columnsList = columnRepository.findAllByBoardIdAndColumnOrderBetween(
                boardId, newOrder, columns.getColumnOrder());
            for (Columns column : columnsList) {
                if (column.equals(columns)) {
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

    //보드 확인
    private Board checkBoard(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.checkBoardDeleted();
        return board;
    }

    // 매니저 권한 확인
    private void validateManager(Board board, User user) {
        if (!boardUserRepository.existsByBoardAndUserAndBoardUserRole(board, user, BoardUser.BoardUserRole.MANAGER)) {
            throw new InvalidUserException(ColumnErrorCode.BOARD_MANAGER_ONLY);
        }
    }

}
