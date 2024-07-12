package b12.trello.domain.user.controller;

import b12.trello.domain.user.dto.SignupRequestDto;
import b12.trello.domain.user.dto.SignupResponseDto;
import b12.trello.domain.user.service.UserService;
import b12.trello.global.response.BasicResponse;
import b12.trello.global.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    @PostMapping("/signup")
    public ResponseEntity<BasicResponse<SignupResponseDto>> signUp(@RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signUp(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BasicResponse.of(HttpStatus.CREATED.value(), "회원가입이 완료되었습니다.", responseDto));
    }

    @DeleteMapping("/signout")
    public ResponseEntity<BasicResponse<SignupResponseDto>> signOut(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        SignupResponseDto responseDto = userService.signOut(userDetails);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(BasicResponse.of(HttpStatus.ACCEPTED.value(), "회원탈퇴가 완료되었습니다.", responseDto));
    }

    @DeleteMapping("/logout")
    public ResponseEntity<BasicResponse<SignupResponseDto>> logOut(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        SignupResponseDto responseDto = userService.logOut(userDetails);

        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(BasicResponse.of(HttpStatus.ACCEPTED.value(), "로그아웃이 완료되었습니다.", responseDto));
    }




}
