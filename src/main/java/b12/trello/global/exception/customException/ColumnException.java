package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;

public class ColumnException extends GlobalCustomException {
    public ColumnException(ErrorCode errorCode) {super(errorCode);}
}
