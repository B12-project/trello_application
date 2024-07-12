package b12.trello.domain.column.repository;

import b12.trello.domain.column.entity.Columns;
import b12.trello.global.exception.customException.CardException;
import b12.trello.global.exception.errorCode.CardErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ColumnRepository extends JpaRepository<Columns, Long> {
    default Columns findColumnsByIdOrElseThrow(Long columnId) {
        return findById(columnId).orElseThrow(() ->
                new CardException(CardErrorCode.COLUMN_NOT_FOUND));
    }
}
