package b12.trello.domain.board.controller;

import b12.trello.domain.board.dto.BoardInviteRequestDto;
import b12.trello.domain.board.dto.BoardRequestDto;
import b12.trello.domain.board.dto.BoardResponseDto;
import b12.trello.domain.board.service.BoardService;
import b12.trello.domain.user.entity.User;
import b12.trello.global.response.BasicResponse;
import b12.trello.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {

    private final BoardService boardService;

    // 보드 등록
    @PostMapping
    public ResponseEntity<BasicResponse<BoardResponseDto>> addBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto boardRequestDto
    ) {
        BoardResponseDto boardResponseDto = boardService.addBoard(boardRequestDto, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BasicResponse
                        .of(HttpStatus.CREATED.value(), "보드 등록 성공", boardResponseDto));
    }

    // 보드들 전체 조회
    @GetMapping
    public ResponseEntity<BasicResponse<List<BoardResponseDto>>> findAll() {
        List<BoardResponseDto> boards = boardService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "보드들 전체 조회 성공", boards));
    }

    // 특정 boardId를 이용한 보드 상세 조회
    @GetMapping("/{boardId}")
    public ResponseEntity<BasicResponse<BoardResponseDto>> getBoardById(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("boardId") Long boardId
    ) {
        BoardResponseDto boardResponseDto = boardService.findById(boardId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "보드 상세 조회 성공", boardResponseDto));
    }

    // 로그인 된 유저가 참여중인 보드 전체 조회
    @GetMapping("/invitation")
    public ResponseEntity<BasicResponse<List<BoardResponseDto>>> getUserBoards(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<BoardResponseDto> boards = boardService.getBoardsByUser(userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "사용자가 참여 중인 보드 전체 조회", boards));
    }

    // 로그인 된 유저가 매니저인 경우 매니징하고 있는 전체 보드 조회
    @GetMapping("/manager")
    public ResponseEntity<BasicResponse<List<BoardResponseDto>>> getManagedBoards(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<BoardResponseDto> boards = boardService.getBoardsManagedByUser(userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "사용자가 매니징 중인 보드 조회 성공", boards));
    }


    // 보드 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<BasicResponse<BoardResponseDto>> updateBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardRequestDto boardRequestDto,
            @PathVariable("boardId") Long boardId
    ) {
        BoardResponseDto boardResponseDto = boardService.updateBoard(boardRequestDto, boardId, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "보드 수정 성공", boardResponseDto));
    }

    // 보드 삭제 (DeletedAt 으로 변경 )
    @DeleteMapping("/{boardId}")
    public ResponseEntity<BasicResponse<Void>> deleteBoard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable("boardId") Long boardId
    ) {
        boardService.deleteBoard(boardId, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "보드 삭제 성공"));
    }

    // 매니저가 초대할 유저 조회
    @GetMapping("/search/users")
    public ResponseEntity<BasicResponse<List<String>>> getInviteableUsers(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<String> users = boardService.findAllUserEmails();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "초대할 수 있는 유저 조회 성공", users));
    }

    // 보드로 사용자 초대해서 참여자로 만들기 (이메일로 초대)
    @PostMapping("/invitation")
    public ResponseEntity<BasicResponse<Void>> inviteUserByEmail(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody BoardInviteRequestDto boardInviteRequestDto
    ) {
        // 이메일로 사용자 찾기
        User invitedUser = boardService.findByEmail(boardInviteRequestDto.getUserEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        // 초대 기능 호출
        boardService.inviteUserByEmail(boardInviteRequestDto.getBoardId(), invitedUser, userDetails.getUser());

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse
                        .of(HttpStatus.OK.value(), "사용자 초대 성공"));
    }

    // 사용자 매니저로 승격
//    @PostMapping("/manager")
//    public ResponseEntity<BasicResponse<BoardResponseDto>> promoteToManager(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @Valid @RequestBody BoardInviteRequestDto boardInviteRequestDto
//    ) {
//        boardService.promoteToManager(boardInviteRequestDto, userDetails.getUser());
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(BasicResponse
//                        .of(HttpStatus.OK.value(), "사용자 매니저 승격 성공"));
//    }

}
