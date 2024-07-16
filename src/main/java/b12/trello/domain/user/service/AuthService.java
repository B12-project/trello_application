package b12.trello.domain.user.service;

import b12.trello.domain.user.entity.User;
import b12.trello.domain.user.repository.UserRepository;
import b12.trello.global.exception.customException.UserException;
import b12.trello.global.exception.errorCode.UserErrorCode;
import b12.trello.global.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public void updateRefreshToken(String email, String refreshToken) {
        User user = userRepository.findByEmailOrElseThrow(email);
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }


    public String reissueAccessToken(String refreshToken, HttpServletResponse response) {

        Date date = new Date();

        if (refreshToken == null) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN); // 리프레시 토큰을 찾을 수 없습니다.
        }

        refreshToken = jwtUtil.substringToken(refreshToken);
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new UserException(UserErrorCode.EXPIRED_REFRESH_TOKEN); // 리프레시 토큰 유효기간이 지났습니다. 재 로그인 필요
        }

        String email = jwtUtil.extractEmail(refreshToken);


        // DB에 저장된 refresh token 이 동일한지 확인
        User user = userRepository.findByEmailOrElseThrow(email);

        if(!refreshToken.equals(user.getRefreshToken())) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN); // 동일한 리프레시 토큰이 아닙니다.
        }


        // access/refresh token 재발급
        String newAccessToken = jwtUtil.createAccessToken(user.getEmail());
        String newRefreshToken = jwtUtil.createRefreshToken(user.getEmail());

        jwtUtil.addJwtToHeader(response, JwtUtil.AUTHORIZATION_HEADER, newAccessToken);
//         refreshToken HTTPOnly 쿠키에 저장
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .sameSite("Strict")
                .build();
        response.setHeader("Set-Cookie", refreshTokenCookie.toString());

        user.updateRefreshToken(newRefreshToken);
        userRepository.save(user);

        return newAccessToken;
    }
}
