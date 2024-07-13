package b12.trello.global.exception.exceptionHandler;

import b12.trello.global.exception.customException.GlobalCustomException;
import b12.trello.global.exception.errorCode.CommonErrorCode;
import b12.trello.global.response.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Validation 예외 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleValidException(MethodArgumentNotValidException e) {
        log.error("[MethodArgumentNotValidException] caused By : {}, message : {} ", NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());

        Map<String, String> errors = e.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (existingValue, newValue) -> existingValue
                ));

        ObjectMapper mapper = new ObjectMapper();
        String response = "";
        try {
            response = mapper.writeValueAsString(errors);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        ErrorResponse errorResponse = ErrorResponse.of(CommonErrorCode.BAD_REQUEST, response);

        return ResponseEntity.status(CommonErrorCode.BAD_REQUEST.getHttpStatusCode())
                .body(errorResponse);
    }

    /**
     * CustomException 예외 처리
     */
    @ExceptionHandler(GlobalCustomException.class)
    protected ResponseEntity<ErrorResponse> handlerGlobalCustomException(GlobalCustomException e) {
        log.error("{} 예외 발생", e.getClass());
        return ResponseEntity.status(e.getErrorCode().getHttpStatusCode())
                .body(ErrorResponse.of(e.getErrorCode()));
    }

    /**
     * 기타 예외 처리
     */
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("{} 예외 발생, caused By: {} , message: {}", e.getClass(), NestedExceptionUtils.getMostSpecificCause(e), e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ErrorResponse.of(CommonErrorCode.INTERNAL_SERVER_ERROR));
    }

}
