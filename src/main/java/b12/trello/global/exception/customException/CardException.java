package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;

public class CardException extends GlobalCustomException{
    public CardException(ErrorCode errorCode) {
        super(errorCode);
    }
}
