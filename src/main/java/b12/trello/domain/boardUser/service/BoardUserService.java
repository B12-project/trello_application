package b12.trello.domain.boardUser.service;

import b12.trello.domain.board.dto.BoardUserResponseDto;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardUserService {

    private final BoardUserRepository boardUserRepository;

    public List<BoardUserResponseDto> findUsersInBoard(Long boardId) {
        return boardUserRepository.findByBoardId(boardId).stream().map(boardUser -> BoardUserResponseDto.builder()
                .userId(boardUser.getUser().getId())
                .userName(boardUser.getUser().getName())
                .email(boardUser.getUser().getEmail())
                .build())
                .collect(Collectors.toList());
    }
}
