package b12.trello.domain.column.controller;

import b12.trello.domain.column.dto.ColumnCreateRequestDto;
import b12.trello.domain.column.dto.ColumnFindResponseDto;
import b12.trello.domain.column.dto.ColumnFindRequestDto;
import b12.trello.domain.column.dto.ColumnModifyRequestDto;
import b12.trello.domain.column.dto.ColumnOrderModifyRequestDto;
import b12.trello.domain.column.service.ColumnService;
import b12.trello.global.response.BasicResponse;
import b12.trello.global.security.UserDetailsImpl;
import java.util.List;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/columns")
@RestController
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    //컬럼 생성
    @PostMapping
    public ResponseEntity<BasicResponse<Void>> createColumn(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ColumnCreateRequestDto requestDto
    ) {

        columnService.createColumn(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 생성 완료"));
    }


    //컬럼 목록 조회
    @GetMapping
    public ResponseEntity<BasicResponse<List<ColumnFindResponseDto>>> findColumns(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long boardId
    ) {

        List<ColumnFindResponseDto> responseDto = columnService.findColumns(userDetails.getUser(), boardId);

        return ResponseEntity.status(HttpStatus.OK).body(BasicResponse.of("컬럼목록", responseDto));
    }

    //컬럼 수정
    @PatchMapping("/{columnId}")
    public ResponseEntity<BasicResponse<Void>> modifyColumn(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long columnId,
            @RequestBody ColumnModifyRequestDto requestDto
    ) {
        columnService.modifyColumn(userDetails.getUser(), columnId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(BasicResponse.of("컬럼 수정 완료"));
    }

    //컬럼 삭제
    @DeleteMapping("/{columnId}")
    public ResponseEntity<BasicResponse<Void>> deleteColumn(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long columnId
    ) {
        columnService.deleteColumn(userDetails.getUser(), columnId);

        return ResponseEntity.status(HttpStatus.OK).body(BasicResponse.of("컬럼 삭제 완료"));
    }

    //컬럼 순서 변경
    @PatchMapping("/{columnId}/order")
    public ResponseEntity<BasicResponse<Void>> modifyColumnOrder(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long columnId,
            @Valid @RequestBody ColumnOrderModifyRequestDto requestDto
    ) {
        columnService.modifyColumnOrder(userDetails.getUser(), columnId, requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(BasicResponse.of("컬럼 순서 변경 완료"));
    }

}
