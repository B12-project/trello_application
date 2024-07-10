package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class GlobalCustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public GlobalCustomException(ErrorCode errorCode) {
        super(errorCode.getErrorDescription());
        this.errorCode = errorCode;
    }
}