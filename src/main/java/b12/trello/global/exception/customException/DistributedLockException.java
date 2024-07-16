package b12.trello.global.exception.customException;

import b12.trello.global.exception.errorCode.ErrorCode;

public class DistributedLockException extends GlobalCustomException{
    public DistributedLockException(ErrorCode errorCode) {
        super(errorCode);
    }
}
