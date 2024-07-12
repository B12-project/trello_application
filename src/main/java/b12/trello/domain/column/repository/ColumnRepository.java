package b12.trello.domain.column.repository;

import b12.trello.domain.column.entity.Columns;
import b12.trello.global.exception.customException.column.ColumnDuplicatedException;
import b12.trello.global.exception.customException.column.ColumnNotFoundException;
import b12.trello.global.exception.errorCode.column.ColumnErrorCode;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

    Long countByBoardId(Long boardId);

    List<Columns> findAllByBoardIdOrderByColumnOrderAsc(Long boardId);

    boolean existsByColumnNameAndBoardId(String columnName, Long boardId);

    List<Columns> findAllByBoardIdAndColumnOrderBetween(Long boardId, Long newOrder, Long columnOrder);

    List<Columns> findAllByBoardIdAndColumnOrderGreaterThan(Long id, Long columnOrder);

    default void existsByColumnNameAndBoardIdOrElseThrow(String columnName, Long boardId){
        if(existsByColumnNameAndBoardId(columnName, boardId)){
            throw new ColumnDuplicatedException(ColumnErrorCode.COLUMN_ALREADY_REGISTERED_ERROR);
        }
    }
    default Columns findByIdOrElseThrow(Long columnId){
        return findById(columnId).orElseThrow(() -> new ColumnNotFoundException(ColumnErrorCode.COLUMN_NOT_FOUND));
    }


}
