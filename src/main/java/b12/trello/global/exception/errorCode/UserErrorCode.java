package b12.trello.global.exception.errorCode;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorCode implements ErrorCode {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND.value(), "해당 유저를 찾을 수 없습니다."),
    EMAIL_DUPLICATED(HttpStatus.BAD_REQUEST.value(), "이미 가입된 이메일입니다."),
    VALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED.value(), "토큰이 아직 유효 합니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED.value(), "리프레시 토큰을 찾을 수 없습니다."),
    INVALID_ADMIN_TOKEN(HttpStatus.UNAUTHORIZED.value(), "유효한 ADMIN TOKEN값이 아닙니다."),
    SIGN_OUT_USER(HttpStatus.BAD_REQUEST.value(), "탈퇴한 계정입니다.");

    private final int httpStatusCode;
    private final String errorDescription;
}