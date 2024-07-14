package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    MANAGER_PERMISSION_REQUIRED(HttpStatus.BAD_REQUEST.value(), "매니저 권한이 있는 사용자만 보드를 생성할 수 있습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 보드가 존재하지 않습니다."),
    DELETED_BOARD(HttpStatus.BAD_REQUEST.value(), "해당 보드는 삭제되었습니다.");


    private final int httpStatusCode;
    private final String errorDescription;
}