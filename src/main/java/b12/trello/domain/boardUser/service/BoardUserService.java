package b12.trello.domain.boardUser.service;

import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.UserException;
import b12.trello.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardUserService {
    private final BoardUserRepository boardUserRepository;

    public User findBoardUser(Long boardId, Long userId) {
        BoardUser boardUser = boardUserRepository.findByBoardIdAndUserId(boardId, userId).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));

        return boardUser.getUser();
    }
}
