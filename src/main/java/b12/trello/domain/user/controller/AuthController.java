package b12.trello.domain.user.controller;

import b12.trello.domain.user.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/refresh")
    public ResponseEntity<String> reissueToken(HttpServletRequest request, HttpServletResponse response) {
        String newAccessToken = authService.reissueAccessToken(request, response);
        return new ResponseEntity<>(newAccessToken, HttpStatus.OK);
    }
}
