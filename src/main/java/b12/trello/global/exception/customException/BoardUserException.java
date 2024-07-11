package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;

public class BoardUserException extends GlobalCustomException{
    public BoardUserException(ErrorCode errorCode) {
        super(errorCode);
    }
}
