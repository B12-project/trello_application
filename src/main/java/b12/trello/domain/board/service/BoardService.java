package b12.trello.domain.board.service;

import b12.trello.domain.board.dto.BoardInviteRequestDto;
import b12.trello.domain.board.dto.BoardRequestDto;
import b12.trello.domain.board.dto.BoardResponseDto;
import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import b12.trello.domain.user.entity.User;
import b12.trello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;
    private final UserRepository userRepository;

    public BoardResponseDto addBoard(BoardRequestDto boardRequestDto, User user) {
        Board board = new Board(boardRequestDto, user);
        boardRepository.save(board);

        // 보드를 생성한 유저를 매니저로 추가
        BoardUser boardUser = new BoardUser(board, user, BoardUser.BoardUserRole.MANAGER);
        boardUserRepository.save(boardUser);

        return new BoardResponseDto(board);
    }

    public BoardResponseDto findById(long id) {
        Board board = findBoardById(id);
        return new BoardResponseDto(board);
    }

    private Board findBoardById(long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드가 존재하지 않습니다."));
    }

    public List<BoardResponseDto> findAll() {
        List<Board> boardlist = boardRepository.findAll();
        return boardlist.stream()
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto updateBoard(BoardRequestDto boardRequestDto, Long boardId, User user) {
        Board board = findBoardById(boardId);

        validateManager(board, user);

        board.update(boardRequestDto);
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    // TODO: 소프트 딜리트로 변경하기
    public void deleteBoard(Long boardId, User user) {
        Board board = findBoardById(boardId);

        validateManager(board, user);

        boardRepository.delete(board);
    }

//    public void inviteUser(BoardInviteRequestDto boardInviteRequestDto, User inviter) {
//        Board board = findBoardById(boardInviteRequestDto.getBoardId());
//
//        if (!isManager(inviter)) {
//            throw new IllegalArgumentException("매니저만 사용자를 초대할 수 있습니다.");
//        }
//
//        User user = userRepository.findByEmail(boardInviteRequestDto.getUserEmail())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
//
//        if (boardUserRepository.existsByBoardAndUser(board, user)) {
//            throw new IllegalArgumentException("이미 초대된 사용자입니다.");
//        }
//
//        BoardUser boardUser = new BoardUser(board, user, BoardUser.BoardUserRole.INVITEE);
//        boardUserRepository.save(boardUser);
//    }


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void inviteUserByEmail(Long boardId, User invitedUser, User inviter) {
        Board board = findBoardById(boardId);

        if (!isManager(inviter)) {
            throw new IllegalArgumentException("매니저만 사용자를 초대할 수 있습니다.");
        }

        if (boardUserRepository.existsByBoardAndUser(board, invitedUser)) {
            throw new IllegalArgumentException("이미 초대된 사용자입니다.");
        }

        BoardUser boardUser = new BoardUser(board, invitedUser, BoardUser.BoardUserRole.INVITEE);
        boardUserRepository.save(boardUser);
    }


//    public void promoteToManager(BoardInviteRequestDto boardInviteRequestDto, User promoter) {
//        Board board = findBoardById(boardInviteRequestDto.getBoardId());
//
//        validateManager(board, promoter);
//
//        User user = userRepository.findByEmail(boardInviteRequestDto.getUserId().toString())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));
//
//        BoardUser boardUser = boardUserRepository.findByBoardAndUser(board, user)
//                .orElseThrow(() -> new IllegalArgumentException("해당 사용자는 이 보드의 멤버가 아닙니다."));
//
//        boardUser.setBoardUserRole(BoardUser.BoardUserRole.MANAGER);
//        boardUserRepository.save(boardUser);
//    }

    public List<String> findAllUserEmails() {
        return userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    // 특정 유저가 참여 있는 보드 조회
    public List<BoardResponseDto> getBoardsByUser(User user) {
        List<BoardUser> boardUsers = boardUserRepository.findByUser(user);
        return boardUsers.stream()
                .map(boardUser -> new BoardResponseDto(boardUser.getBoard()))
                .collect(Collectors.toList());
    }

    // 특정 유저가 매니징 하고 있는 보드 조회
    public List<BoardResponseDto> getBoardsManagedByUser(User user) {
        List<BoardUser> boardUsers = boardUserRepository.findByUserAndBoardUserRole(user, BoardUser.BoardUserRole.MANAGER);
        return boardUsers.stream()
                .map(boardUser -> new BoardResponseDto(boardUser.getBoard()))
                .collect(Collectors.toList());
    }

    private void validateManager(Board board, User user) {
        BoardUser boardUser = boardUserRepository.findByBoardAndUser(board, user)
                .orElseThrow(() -> new IllegalArgumentException("보드 매니저만 할 수 있습니다."));
        if (boardUser.getBoardUserRole() != BoardUser.BoardUserRole.MANAGER) {
            throw new IllegalArgumentException("보드 매니저만 할 수 있습니다.");
        }
    }

    private boolean isManager(User user) {
        return boardUserRepository.existsByUserAndBoardUserRole(user, BoardUser.BoardUserRole.MANAGER);
    }

    private boolean isBoardMember(Board board, User user) {
        return boardUserRepository.existsByBoardAndUser(board, user);
    }
}
