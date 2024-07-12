package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ColumnErrorCode implements ErrorCode {

    COLUMN_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 컬럼이 존재하지 않습니다."),
    COLUMN_ALREADY_REGISTERED_ERROR(HttpStatus.BAD_REQUEST.value(), "중복된 컬럼명입니다."),
    INVALID_ORDER(HttpStatus.BAD_REQUEST.value(), "순서 입력값이 올바르지 않습니다"),
    BOARD_MANAGER_ONLY(HttpStatus.BAD_REQUEST.value(), "보드 매니저가 아닙니다.");

    private final int httpStatusCode;
    private final String errorDescription;

}