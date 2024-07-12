package b12.trello.domain.board.dto;

import b12.trello.domain.board.entity.Board;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {

    private Long id;
    private String boardName;
    private String boardInfo;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    public BoardResponseDto(Board board) {
        this.id = board.getId();
        this.boardName = board.getBoardName();
        this.boardInfo = board.getBoardInfo();
        this.createdAt = board.getCreatedAt();
        this.updatedAt = board.getModifiedAt();
    }



}
