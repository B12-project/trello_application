package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum DistributedLockErrorCode implements ErrorCode {
    LOCK_NOT_AVAILABLE(HttpStatus.BAD_REQUEST.value(), "락에 진입할 수 없습니다."),
    LOCK_INTERRUPTED_ERROR(HttpStatus.BAD_REQUEST.value(), "락 진입 시도 중 인터럽트가 발생했습니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}
