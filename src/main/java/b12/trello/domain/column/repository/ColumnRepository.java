package b12.trello.domain.column.repository;

import b12.trello.domain.column.entity.Columns;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

    Long countByBoardId(Long boardId);

    List<Columns> findAllByBoardIdOrderByColumnOrderAsc(Long boardId);

    boolean existsByColumnNameAndBoardId(String columnName, Long boardId);

    List<Columns> findAllByBoardIdAndColumnOrderBetween(Long boardId, Long newOrder, Long columnOrder);

    List<Columns> findAllByBoardIdAndColumnOrderGreaterThan(Long id, Long columnOrder);
}
