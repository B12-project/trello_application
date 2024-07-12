package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum BoardErrorCode implements ErrorCode {
    MANAGER_PERMISSION_REQUIRED(HttpStatus.BAD_REQUEST.value(), "매니저 권한이 있는 사용자만 보드를 생성할 수 있습니다."),
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 보드가 존재하지 않습니다."),
    BOARD_MEMBER_ONLY(HttpStatus.BAD_REQUEST.value(), "보드의 멤버만 사용자를 초대할 수 있습니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "존재하지 않는 사용자입니다."),
    USER_ALREADY_INVITED(HttpStatus.BAD_REQUEST.value(), "이미 초대된 사용자입니다."),
    BOARD_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 사용자는 이 보드의 멤버가 아닙니다."),
    BOARD_MANAGER_ONLY(HttpStatus.BAD_REQUEST.value(), "보드 매니저만 할 수 있습니다."),
    DELETED_BOARD(HttpStatus.BAD_REQUEST.value(), "해당 보드는 삭제되었습니다.");


    private final int httpStatusCode;
    private final String errorDescription;
}