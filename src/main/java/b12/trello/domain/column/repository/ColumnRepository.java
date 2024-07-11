package b12.trello.domain.column.repository;

import b12.trello.domain.column.entity.Columns;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Columns, Long> {

    List<Columns> findAllByColumnOrderBetween(Long newOrder, Long columnOrder);

    Long countByBoardId(Long boardId);

    List<Columns> findAllByColumnOrderGreaterThan(Long columnOrder);

    List<Columns> findAllByBoardIdOrderByColumnOrderAsc(Long boardId);

}
