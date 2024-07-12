package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;

public class BoardException extends GlobalCustomException {
    public BoardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
