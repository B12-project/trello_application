package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;
import lombok.Getter;

@Getter
public class UserException extends GlobalCustomException {
    public UserException(ErrorCode errorCode) {
        super(errorCode);
    }
}