package b12.trello.domain.user.controller;

import b12.trello.domain.user.dto.ProfileRequestDto;
import b12.trello.domain.user.dto.ProfileResponseDto;
import b12.trello.domain.user.dto.SignupRequestDto;
import b12.trello.domain.user.dto.SignupResponseDto;
import b12.trello.domain.user.service.UserService;
import b12.trello.global.response.BasicResponse;
import b12.trello.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
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
    public ResponseEntity<BasicResponse<SignupResponseDto>> signUp(@Valid @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.signUp(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BasicResponse.of(HttpStatus.CREATED.value(), "회원가입이 완료되었습니다.", responseDto));
    }

    @DeleteMapping("/signout")
    public ResponseEntity<BasicResponse<SignupResponseDto>> signOut(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        SignupResponseDto responseDto = userService.signOut(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "회원탈퇴가 완료되었습니다.", responseDto));
    }

    // 0713 patch로 진행 시 오류가 남
    @PutMapping("/logout")
    public ResponseEntity<BasicResponse<SignupResponseDto>> logOut(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        SignupResponseDto responseDto = userService.logOut(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "로그아웃이 완료되었습니다.", responseDto));
    }

    @GetMapping("/profile")
    public ResponseEntity<BasicResponse<ProfileResponseDto>> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        ProfileResponseDto responseDto = userService.getProfile(userDetails.getUser());

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "회원정보를 조회했습니다.", responseDto));
    }

    @PutMapping("/profile")
    public ResponseEntity<BasicResponse<ProfileResponseDto>> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestBody ProfileRequestDto requestDto) {
        ProfileResponseDto responseDto = userService.updateProfile(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "회원정보를 수정했습니다.", responseDto));
    }

    @PutMapping("/password")
    public ResponseEntity<BasicResponse<SignupResponseDto>> updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                           @Valid  @RequestBody SignupRequestDto requestDto) {
        SignupResponseDto responseDto = userService.updatePassword(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "비밀번호를 수정했습니다.", responseDto));
    }

}
