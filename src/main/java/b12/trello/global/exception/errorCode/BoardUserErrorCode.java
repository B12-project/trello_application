package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardUserErrorCode implements ErrorCode{
    BOARD_USER_FORBIDDEN(HttpStatus.BAD_REQUEST.value(), "보드 사용자가 아니므로 접근할 수 없습니다."),
    BOARD_USER_DUPLICATED(HttpStatus.BAD_REQUEST.value(), "이미 존재하는 보드 사용자입니다."),
    BOARD_USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "보드에 해당 유저가 존재하지 않습니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
