package b12.trello.global.security.filter;

import b12.trello.domain.user.dto.LoginRequestDto;
import b12.trello.domain.user.entity.User;
import b12.trello.domain.user.repository.UserRepository;
import b12.trello.domain.user.service.AuthService;
import b12.trello.global.security.UserDetailsImpl;
import b12.trello.global.security.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

@Slf4j(topic = "로그인 및 JWT 생성")
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    // refreshToken 저장을 위해
    AuthService authService;

    private PasswordEncoder passwordEncoder;

    UserRepository userRepository;

    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtAuthenticationFilter(JwtUtil jwtUtil, AuthService authService) {
        this.jwtUtil = jwtUtil;
        this.authService = authService;
        setFilterProcessesUrl("/users/login");
    }

    // 로그인 시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("로그인 시도");

        // 입력받은 요청이 json 형태인지 확인
        if("application/json".equals(request.getContentType())) {
            try {
                // 요청 받은 json -> 객체로 변환
                LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
                // 요청 받은 값 확인
                log.info("로그인 요청을 받았습니다: " + loginRequestDto.getEmail() + "     " + loginRequestDto.getPassword());

                Optional<User> optionalUser = userRepository.findByEmail(loginRequestDto.getEmail());

                // 회원가입 된 username 이 있는지 확인
                if(optionalUser.isPresent()) {
                    User user = optionalUser.get();

                    // 로그인 시도 한 user 가 탈퇴 상태인지 확인 (지금은 임시로 user) + 비밀번호 확인
                    if (User.UserAuth.DELETED.equals(user.getAuth())) {
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write("탈퇴한 계정입니다.");
                    } else if (user.getPassword().equals(loginRequestDto.getPassword())) {
                        response.getWriter().write("비밀번호가 틀렸습니다.");
                    }
                } else {
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("해당 이메일로 가입한 계정이 없습니다.");
                }

                UsernamePasswordAuthenticationToken authRequest =
                        new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword());

                // 추가적인 요청정보 설정
                setDetails(request, authRequest);

                // AuthenticationManager 를 통해서 인증 시도
                return this.getAuthenticationManager().authenticate(authRequest);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        // 요청이 json 형식이 아닐 경우에는 부모클래스 기본 동작 수행
        return super.attemptAuthentication(request, response);
    }

    // 로그인 성공 시 인증 객체를 받아옴
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공 JWT 생성");
        Long id = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getId();
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        User.UserAuth auth = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getAuth();

        String accessToken = jwtUtil.createAccessToken(username, auth);
        String refreshToken = jwtUtil.createRefreshToken();

        // 토큰을 헤더에 전달
        jwtUtil.addJwtToHeader(response, JwtUtil.ACCESS_TOKEN_HEADER, accessToken);

        // 헤더 username 추가
        response.setHeader("username", username);

        // refreshToken Entity 에 저장
        authService.updateRefreshToken(id, refreshToken);

        // 확인용
        log.info("accessToken: " + accessToken);
        log.info("refreshToken: " + refreshToken);
        log.info("username: " + username);

        // 로그인 성공 메세지
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("\"로그인 성공!!\"");
    }

    // 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException {
        log.info("로그인 실패");
        // 로그인 실패 메세지
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("로그인 실패");
        // 인증실패 401코드
        response.setStatus(401);
    }
}
