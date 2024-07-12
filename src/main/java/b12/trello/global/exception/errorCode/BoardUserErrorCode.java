package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardUserErrorCode implements ErrorCode{

    BOARD_USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "보드 사용자 정보를 찾을 수 없습니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
