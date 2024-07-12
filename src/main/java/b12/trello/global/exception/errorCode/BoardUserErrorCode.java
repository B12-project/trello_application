package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardUserErrorCode implements ErrorCode{

    BOARD_USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 사용자입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
