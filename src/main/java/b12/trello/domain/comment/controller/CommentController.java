package b12.trello.domain.comment.controller;

import b12.trello.domain.comment.dto.CommentModifyRequestDto;
import b12.trello.domain.comment.dto.CommentRequestDto;
import b12.trello.domain.comment.dto.CommentResponseDto;
import b12.trello.domain.comment.service.CommentService;
import b12.trello.global.response.BasicResponse;
import b12.trello.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<BasicResponse<Void>> createComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CommentRequestDto requestDto
    ) {
        commentService.createComment(requestDto, userDetails.getUser());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BasicResponse.of(HttpStatus.CREATED.value(), "댓글 등록 성공"));
    }

    // 댓글 전체 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<BasicResponse<List<CommentResponseDto>>> findAllByCardId(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId
    ) {
        List<CommentResponseDto> comments = commentService.findAllByCardId(userDetails.getUser(), cardId);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "댓글 전체 조회 성공", comments));
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<BasicResponse<Void>> modifyComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentModifyRequestDto requestDto
    ){
        commentService.modifyComment(commentId, requestDto, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "댓글 수정 성공"));
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<BasicResponse<Void>> deleteComment(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long commentId
    ) {
        commentService.deleteComment(commentId, userDetails.getUser());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BasicResponse.of(HttpStatus.OK.value(), "댓글 삭제 성공"));
    }
}
