package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "댓글을 찾을 수 없습니다."),
    ACCESS_DENIED(HttpStatus.UNAUTHORIZED.value(), "본인의 댓글에만 접근 가능합니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}