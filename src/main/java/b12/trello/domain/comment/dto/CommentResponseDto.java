package b12.trello.domain.comment.dto;

import b12.trello.domain.comment.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class CommentResponseDto {


    private Long id;
    private Long cardId;
    private String username;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.cardId = comment.getCard().getId();
        this.username = comment.getUser().getName();
        this.content = comment.getContents();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getModifiedAt();
    }
}