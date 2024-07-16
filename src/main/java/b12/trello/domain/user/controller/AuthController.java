package b12.trello.domain.user.controller;

import b12.trello.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    public ResponseEntity<String> reissueToken(
            @CookieValue(name = "refreshToken") String refreshToken,
            HttpServletResponse response) {

        log.info("Refresh token: " + refreshToken);
        String newAccessToken = authService.reissueAccessToken(refreshToken, response);
        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }
}
