package b12.trello.global.exception.customException.column;

import b12.trello.global.exception.customException.GlobalCustomException;
import b12.trello.global.exception.errorCode.ErrorCode;

public class ColumnNotFoundException extends GlobalCustomException{
    public ColumnNotFoundException(ErrorCode errorCode) {super(errorCode);}
}
