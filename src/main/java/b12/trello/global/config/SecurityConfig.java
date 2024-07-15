package b12.trello.global.config;


import b12.trello.domain.user.repository.UserRepository;
import b12.trello.domain.user.service.AuthService;
import b12.trello.global.security.UserDetailsServiceImpl;
import b12.trello.global.security.filter.JwtAuthenticationFilter;
import b12.trello.global.security.filter.JwtAuthorizationFilter;
import b12.trello.global.security.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    private final UserDetailsServiceImpl userDetailsService;

    private final AuthenticationConfiguration authenticationConfiguration;

    private final AuthService authService;

    private final UserRepository userRepository;

//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer(){
//        return web -> {
//            web.ignoring()
//                    .requestMatchers(HttpMethod.GET, "/users/refresh");
//        };
//    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtUtil,authService,userRepository);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtUtil, userDetailsService);
    }


    // 시큐리티를 사용할 때 특정 URL 통과, 기능 선택
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // CSRF 설정 (사용하지 않아서 disable)
        http.csrf((csrf) -> csrf.disable()); // yellow: 람다는 메소드 참조로 대체 가능

        // 기본 설정인 Session 방식에서 JWT 방식을 사용하기 위한 설정
        http.sessionManagement((sessionManagement) ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );


        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll() // resources 접근 허용 설정
                        .requestMatchers("/").permitAll() // 메인 페이지 요청 허가
                        .requestMatchers("/users/page/**").permitAll()
                        .requestMatchers("/page/**").permitAll()
                        .requestMatchers("/users/signup").permitAll()
                        .requestMatchers("/users/login").permitAll() // /users/** 로 시작하는 요청 모두 접근 허가 (스웨거의 접근도 여기서 허용 가능) (ex 특정 권한이 있는 사용자만 접근 가능하게도 설정 가능)
                        .requestMatchers("/users/refresh").permitAll() // Refresh Token URL 인가 처리 없음. //
                        .anyRequest().authenticated() // 위의 요청 제외 모든 요청은 인증처리가 필요
        );

        http.formLogin((formLogin) ->
                formLogin
                        .loginPage("/users/page/login").permitAll()
        );

        // 필터 관리 (동작 순서 지정)
        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // 페스워드 인코딩
    @Bean
    public PasswordEncoder passwordEncoder() {
        // BCrypt = 비밀번호를 암호화 해주는 Hash 함수(강력한 암호화)
        return new BCryptPasswordEncoder();
    }


}
