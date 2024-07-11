package b12.trello.domain.column.controller;

import b12.trello.domain.column.dto.ColumnCreateRequestDto;
import b12.trello.domain.column.dto.ColumnFindResponseDto;
import b12.trello.domain.column.dto.ColumnFindRequestDto;
import b12.trello.domain.column.dto.ColumnModifyRequestDto;
import b12.trello.domain.column.service.ColumnService;
import b12.trello.global.response.BasicResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/columns")
@RestController
@RequiredArgsConstructor
public class ColumnController {

    private final ColumnService columnService;

    //컬럼 생성
    @PostMapping
    public ResponseEntity<BasicResponse<Void>> createColumn (@RequestBody ColumnCreateRequestDto requestDto) {

        columnService.createColumn(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 생성 완료"));
    }


    //컬럼 목록 조회
    @GetMapping
    public ResponseEntity<BasicResponse<List<ColumnFindResponseDto>>> findColumns (@RequestBody ColumnFindRequestDto requestDto) {

        List<ColumnFindResponseDto> responseDto = columnService.findColumns(requestDto);

        return ResponseEntity.status(HttpStatus.OK).body(BasicResponse.of("컬럼목록", responseDto));
    }
    //컬럼 수정
    @PatchMapping("/{columnId}")
    public ResponseEntity<BasicResponse<Void>> modifyColumn (@PathVariable Long columnId, @RequestBody ColumnModifyRequestDto requestDto) {

        columnService.modifyColumn(columnId, requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 수정 완료"));
    }
    //컬럼 삭제
    @DeleteMapping("/{columnId}")
    public ResponseEntity<BasicResponse<Void>> deleteColumn(@PathVariable Long columnId) {

        columnService.deleteColumn(columnId);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 삭제 완료"));
    }
    //컬럼 순서 변경
    @PatchMapping("/{columnId}/{order}")
    public ResponseEntity<BasicResponse<Void>> modifyColumnOrder(@PathVariable Long columnId, @PathVariable Long order) {

        columnService.modifyColumnOrder(columnId, order);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of("컬럼 순서 변경 완료"));
    }

}
