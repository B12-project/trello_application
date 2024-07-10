package b12.trello.domain.user.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public class UserController {

//    @PostMapping("/signup")
//    public ResponseEntity<BasicResponse<SignUpResponseDto>> signUp(@RequestBody SignUpRequestDto requestDto) {
//        SignUpResponseDto responseDto = userService.signUp(requestDto);
//
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(BasicResponse.of(HttpStatus.CREATED.value(), "회원가입이 완료되었습니다.", responseDto));
//    }
}
