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
import b12.trello.global.exception.errorCode.BoardErrorCode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static b12.trello.global.exception.errorCode.BoardErrorCode.*;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;
    private final UserRepository userRepository;

    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
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
                .orElseThrow(() -> new IllegalArgumentException(BOARD_NOT_FOUND.getErrorDescription()));
    }

    public List<BoardResponseDto> findAll() {
        List<Board> boardlist = boardRepository.findAll();
        return boardlist.stream()
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto modifyBoard(BoardRequestDto boardRequestDto, Long boardId, User user) {
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


    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public void inviteUserByEmail(BoardInviteRequestDto boardInviteRequestDto) {
        Board board = findBoardById(boardInviteRequestDto.getBoardId());

        // 매니저만 초대할 수 있는지 확인
        if (!isManager(boardInviteRequestDto.getInviter())) {
            throw new IllegalArgumentException(BOARD_MANAGER_ONLY.getErrorDescription());
        }

        // 초대할 사용자의 역할 설정
        BoardUser.BoardUserRole role = boardInviteRequestDto.getBoardUserRole();

        // 사용자 이메일로 사용자 찾기
        User invitedUser = findByEmail(boardInviteRequestDto.getInvitedUser().getEmail())
                .orElseThrow(() -> new IllegalArgumentException(BoardErrorCode.USER_NOT_FOUND.getErrorDescription()));

        // 이미 초대된 사용자인지 확인
        if (boardUserRepository.existsByBoardAndUser(board, invitedUser)) {
            throw new IllegalArgumentException(USER_ALREADY_INVITED.getErrorDescription());
        }

        // 초대된 사용자 추가
        BoardUser boardUser = new BoardUser(board, invitedUser, role);
        boardUserRepository.save(boardUser);
    }


    public List<String> findAllUserEmailList() {
        return userRepository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toList());
    }

    // 특정 유저가 참여 있는 보드 조회
    public List<BoardResponseDto> findBoardListByUser(User user) {
        List<BoardUser> boardUsers = boardUserRepository.findByUser(user);
        return boardUsers.stream()
                .map(boardUser -> new BoardResponseDto(boardUser.getBoard()))
                .collect(Collectors.toList());
    }

    // 특정 유저가 매니징 하고 있는 보드 조회
    public List<BoardResponseDto> findBoardListManagedByUser(User user) {
        List<BoardUser> boardUsers = boardUserRepository.findByUserAndBoardUserRole(user, BoardUser.BoardUserRole.MANAGER);
        return boardUsers.stream()
                .map(boardUser -> new BoardResponseDto(boardUser.getBoard()))
                .collect(Collectors.toList());
    }

    private void validateManager(Board board, User user) {
        BoardUser boardUser = boardUserRepository.findByBoardAndUser(board, user)
                .orElseThrow(() -> new IllegalArgumentException(BOARD_MANAGER_ONLY.getErrorDescription()));
        if (boardUser.getBoardUserRole() != BoardUser.BoardUserRole.MANAGER) {
            throw new IllegalArgumentException(BOARD_MANAGER_ONLY.getErrorDescription());
        }
    }

    private boolean isManager(User user) {
        return boardUserRepository.existsByUserAndBoardUserRole(user, BoardUser.BoardUserRole.MANAGER);
    }

    private boolean isBoardMember(Board board, User user) {
        return boardUserRepository.existsByBoardAndUser(board, user);
    }
}
