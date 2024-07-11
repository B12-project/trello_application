package b12.trello.domain.card.controller;

import b12.trello.domain.card.dto.request.CardCreateRequestDto;
import b12.trello.domain.card.service.CardService;
import b12.trello.global.response.BasicResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    @PostMapping
    public ResponseEntity<BasicResponse<Void>> createCard(@Valid @RequestBody CardCreateRequestDto requestDto) {
        cardService.createCard(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BasicResponse.of(HttpStatus.CREATED.value(), "카드가 생성되었습니다."));
    }

}
