package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardErrorCode implements ErrorCode {
    CARD_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 카드가 존재하지 않습니다."),
    INVALID_COLUMN_UPDATE(HttpStatus.BAD_REQUEST.value(), "다른 보드의 컬럼으로는 이동할 수 없습니다."),
    DATETIME_PARSE_ERROR(HttpStatus.BAD_REQUEST.value(), "날짜 형식이 올바르지 않습니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
