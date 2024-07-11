package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CardErrorCode implements ErrorCode {
    CARD_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 카드가 존재하지 않습니다."),
    INVALID_COLUMN_UPDATE(HttpStatus.BAD_REQUEST.value(), "다른 보드의 컬럼으로는 이동할 수 없습니다."),

    // 다른 에러코드로 이동 예정
    COLUMN_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "해당 컬럼은 존재하지 않습니다."),
    BOARD_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "존재하지 않는 보드입니다."),
    BOARD_STATUS_DELETED(HttpStatus.BAD_REQUEST.value(), "해당 보드는 삭제되었습니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
