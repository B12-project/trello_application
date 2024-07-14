package b12.trello.domain.card.controller;

import b12.trello.domain.card.dto.request.CardColumnModifyRequestDto;
import b12.trello.domain.card.dto.request.CardCreateRequestDto;
import b12.trello.domain.card.dto.request.CardListByColumnRequestDto;
import b12.trello.domain.card.dto.request.CardModifyRequestDto;
import b12.trello.domain.card.dto.response.CardListByColumnResponseDto;
import b12.trello.domain.card.dto.response.CardFindResponseDto;
import b12.trello.domain.card.service.CardService;
import b12.trello.global.response.BasicResponse;
import b12.trello.global.security.UserDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    /**
     * 카드 생성
     */
    @PostMapping
    public ResponseEntity<BasicResponse<Void>> createCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CardCreateRequestDto requestDto
    ) {
        cardService.createCard(userDetails.getUser(), requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BasicResponse.of(HttpStatus.CREATED.value(), "카드가 생성되었습니다."));
    }

    /**
     * 단일 카드 상세 조회
     */
    @GetMapping("/{cardId}")
    public ResponseEntity<BasicResponse<CardFindResponseDto>> findCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId
    ) {
        CardFindResponseDto responseDto = cardService.findCard(userDetails.getUser(), cardId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of("카드가 조회되었습니다.", responseDto));
    }

    /**
     * 순수하게 컬럼별로만 카드 가져올 때
     */
//    @GetMapping
//    public ResponseEntity<BasicResponse<CardListByColumnResponseDto>> findCardListByColumn(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestParam Long columnId) {
//        CardListByColumnResponseDto responseDto = cardService.findCardListByColumn(userDetails.getUser(), columnId);
//
//        return ResponseEntity.status(HttpStatus.OK)
//                .body(BasicResponse.of("선택한 컬럼의 카드가 조회되었습니다.", responseDto));
//    }

    /**
     * 컬럼별 카드 리스트 조회 - 사용자 검색 가능
     * 프론트 쪽에서 사용자를 workerEmail로 검색할지, workerId로 검색할지 몰라 둘 다 추가
     * 검색 조건 없이 컬럼별 조회만 할 때는 search=~ 없이
     * workerId 로 검색할 때는 ?columnId=1&search=workerId&workerId=1
     * workerEmail로 검색할 때는 ?columnId=1&search=email&workerEmail=0923@~
     */
    @GetMapping
    public ResponseEntity<BasicResponse<CardListByColumnResponseDto>> searchCardListByColumn(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long columnId,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Long workerId,
            @RequestParam(required = false) String workerEmail
    ) {
        CardListByColumnResponseDto responseDto = cardService.searchCardListByColumn(userDetails.getUser(), columnId, search, workerId, workerEmail);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of("선택한 컬럼의 카드가 조회되었습니다.", responseDto));
    }

    /**
     * 카드 수정
     */
    @PutMapping("/{cardId}")
    public ResponseEntity<BasicResponse<CardFindResponseDto>> modifyCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @Valid @RequestBody CardModifyRequestDto requestDto
    ) {
        CardFindResponseDto responseDto = cardService.modifyCard(userDetails.getUser(), cardId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of("카드가 수정되었습니다.", responseDto));
    }

    /**
     * 카드 컬럼 수정
     */
    @PatchMapping("/{cardId}")
    public ResponseEntity<BasicResponse<Void>> modifyCardColumn(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId,
            @Valid @RequestBody CardColumnModifyRequestDto requestDto
    ) {
        cardService.modifyCardColumn(userDetails.getUser(), cardId, requestDto);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of("카드의 컬럼이 수정되었습니다."));
    }


    /**
     * 카드 삭제
     */
    @DeleteMapping("/{cardId}")
    public ResponseEntity<BasicResponse<Void>> deleteCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId
    ) {
        cardService.deleteCard(userDetails.getUser(), cardId);

        return ResponseEntity.status(HttpStatus.OK)
                .body(BasicResponse.of("카드가 삭제되었습니다."));
    }

}
