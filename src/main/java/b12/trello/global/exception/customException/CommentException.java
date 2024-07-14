package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;

public class CommentException extends GlobalCustomException {
    public CommentException(ErrorCode errorCode) {
        super(errorCode);
    }
}
