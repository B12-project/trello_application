package b12.trello.global.exception.errorCode;

public interface ErrorCode {
    int getHttpStatusCode();
    String getErrorDescription();
}
