package b12.trello.domain.user.service;

import b12.trello.domain.user.entity.User;
import b12.trello.domain.user.repository.UserRepository;
import b12.trello.global.exception.customException.UserException;
import b12.trello.global.exception.errorCode.UserErrorCode;
import b12.trello.global.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    private final JwtUtil jwtUtil;

    @Transactional
    public void updateRefreshToken(Long id, String refreshToken) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }


    public String reissueAccessToken(Long id, HttpServletRequest request, HttpServletResponse response) {

        // 1. header 에 담긴 access token 유효기간 확인.
        String headerAccessToken = jwtUtil.getAccessTokenFromRequest(request);
        headerAccessToken = jwtUtil.substringToken(headerAccessToken);

        Claims accessClaims = jwtUtil.getUserInfoFromToken(headerAccessToken);
        Date accessExp = accessClaims.getExpiration();
        Date date = new Date();
        if(!accessExp.before(date)) {
            throw new UserException(UserErrorCode.VALID_ACCESS_TOKEN); // 토큰이 아직 유효 합니다.
        }

        // 2. refresh token 존재여부 확인
        String headerRefreshToken = jwtUtil.getRefreshTokenFromRequest(request);

        if (headerRefreshToken == null) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN); // 리프레시 토큰을 찾을 수 없습니다.
        }

        // 3. DB에 저장된 refresh token 이 동일한지 확인
        User user = userRepository.findById(id).orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND));
        if(!headerRefreshToken.equals(user.getRefreshToken())) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN); // 동일한 리프레시 토큰이 아닙니다.
        }

        // 4. refresh token 유효기간 확인
        headerRefreshToken = jwtUtil.substringToken(headerRefreshToken);
        Claims refreshClaims = jwtUtil.getUserInfoFromToken(headerRefreshToken);
        Date refreshExp = refreshClaims.getExpiration();

        if (refreshExp.after(date)) {
            throw new UserException(UserErrorCode.INVALID_REFRESH_TOKEN); // 리프레시 토큰 유효기간이 지났습니다.
        }

        // 5. access token 재발급
        String newAccessToken = jwtUtil.createAccessToken(user.getEmail());

        return newAccessToken;
    }
}
