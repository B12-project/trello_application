package b12.trello.global.security.filter;

import b12.trello.domain.user.entity.User;
import b12.trello.global.security.UserDetailsServiceImpl;
import b12.trello.global.security.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j(topic = "JWT 검증 및 인가")
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // AccessToken 가져오기
        String accessToken = jwtUtil.getAccessTokenFromRequest(request);
        log.info("현재주소 : " + request.getRequestURL().toString());
        log.info("accessToken: " + accessToken);
        if(request.getCookies() != null && request.getCookies().length > 0) {
            log.info("쿠키 있음");
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                log.info(cookie.getName() +" : "+ cookie.getValue());
            }
        }

        // 토큰 값이 있는지 확인
        if (StringUtils.hasText(accessToken)
                && !request.getRequestURL().toString().contains("/static/favicon.ico")
                && !request.getRequestURL().toString().contains("/refresh")
        ) {

            // 있으면 JWT 토큰 substring (가공)
            accessToken = jwtUtil.substringToken(accessToken);

            // 토큰 검증 시작
            // SecurityConfig에서 예외처리를 해도 필터는 거치게 된다.

            if (!jwtUtil.validateToken(accessToken)) {
                //여기서 반환
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }


            // 토큰에서 사용자 정보 가져오기
            Claims accessTokenClaims = jwtUtil.getUserInfoFromToken(accessToken);
            String email = accessTokenClaims.getSubject();
            User findUser = userDetailsService.findUserByUserName(email);

            if (findUser.getRefreshToken() != null) {
                // 인증 처리
                try {
                    setAuthentication(accessTokenClaims.getSubject()); // 토큰 내부 값
                } catch (Exception e) {
                    log.error(e.getMessage());
                    return;
                }
            }

        }

        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        String[] excludePath = {"/user/refresh"};
        String path = request.getRequestURI();
        return Arrays.stream(excludePath).anyMatch(path::startsWith);
    }

}
