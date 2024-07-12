package b12.trello.domain.user.controller;

import b12.trello.domain.user.dto.SignupRequestDto;
import b12.trello.domain.user.dto.SignupResponseDto;
import b12.trello.domain.user.service.UserService;
import b12.trello.global.response.BasicResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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






}
