package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommonErrorCode implements ErrorCode{
    DATETIME_PARSE_ERROR(HttpStatus.BAD_REQUEST.value(), "날짜 형식이 올바르지 않습니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST.value(), "올바르지 않은 요청입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버 내부 에러가 발생했습니다."),
    API_URI_NOT_FOUND(HttpStatus.BAD_REQUEST.value(), "지원하지 않는 URI 입니다.");


    private final int httpStatusCode;
    private final String errorDescription;
}
