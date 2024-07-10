package b12.trello.global.exception.exceptionHandler;

import b12.trello.global.exception.customException.GlobalCustomException;
import b12.trello.global.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(GlobalCustomException.class)
    protected ResponseEntity<ErrorResponse> handlerGlobalCustomException(GlobalCustomException e) {
        log.error("{} 예외 발생", e.getClass());
        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

}
