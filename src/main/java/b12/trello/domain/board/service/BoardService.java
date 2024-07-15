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
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardUserRepository boardUserRepository;
    private final UserRepository userRepository;

    public BoardResponseDto createBoard(BoardRequestDto boardRequestDto, User user) {
        Board board = Board.builder()
                .boardName(boardRequestDto.getBoardName())
                .boardInfo(boardRequestDto.getBoardInfo())
                .build();
        boardRepository.save(board);

        BoardUser boardUser = BoardUser.builder()
                .board(board)
                .user(user)
                .boardUserRole(BoardUser.BoardUserRole.MANAGER)
                .build();
        boardUserRepository.save(boardUser);

        return new BoardResponseDto(board);
    }

    public BoardResponseDto findBoardById(User user, long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.checkBoardDeleted();
        boardUserRepository.validateBoardUser(board.getId(), user.getId());
        return new BoardResponseDto(board);
    }

    public List<BoardResponseDto> findAll() {
        List<Board> boardlist = boardRepository.findAll();
        return boardlist.stream()
                .filter(board -> board.getDeletedAt() == null)
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

    public BoardResponseDto modifyBoard(BoardRequestDto boardRequestDto, Long boardId, User user) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.checkBoardDeleted();
        boardUserRepository.validateBoardManager(board, user);

        board.updateBoard(boardRequestDto.getBoardName(), boardRequestDto.getBoardInfo());
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.checkBoardDeleted();
        boardUserRepository.validateBoardManager(board, user);
        boardRepository.deleteById(boardId);
    }

    @Transactional
    public void inviteUserByEmail(BoardInviteRequestDto boardInviteRequestDto, User inviter) {
        Board board = boardRepository.findByIdOrElseThrow(boardInviteRequestDto.getBoardId());

        // 매니저만 초대할 수 있는지 확인
        boardUserRepository.validateBoardManager(board, inviter);

        // 초대할 사용자의 역할 설정
        BoardUser.BoardUserRole role = boardInviteRequestDto.getBoardUserRole();

        // 사용자 이메일로 사용자 찾기
        User foundInvitedUser = userRepository.findByEmailOrElseThrow(boardInviteRequestDto.getUserEmail());

        // 이미 초대된 사용자인지 확인
        boardUserRepository.validateNotBoardUser(board.getId(), foundInvitedUser.getId());

        // 초대된 사용자를 저장
        BoardUser boardUser = BoardUser.builder()
                .board(board)
                .user(foundInvitedUser)
                .boardUserRole(role)
                .build();

        boardUserRepository.save(boardUser);
    }

    public List<String> findAllUserEmailList() {
        return userRepository.findAllByDeletedAtIsNull().stream()
                .map(User::getEmail)
                .toList();
    }

    // 특정 유저가 참여 하고 있는 보드 조회
    public List<BoardResponseDto> findBoardListByUser(User user) {
        List<BoardUser> boardUsers = boardUserRepository.findByUserAndBoardUserRole(user, BoardUser.BoardUserRole.INVITEE);
        return convertToBoardResponseDtoList(boardUsers);
    }

    // 특정 유저가 매니징 하고 있는 보드 조회
    public List<BoardResponseDto> findBoardListManagedByUser(User user) {
        List<BoardUser> boardUsers = boardUserRepository.findByUserAndBoardUserRole(user, BoardUser.BoardUserRole.MANAGER);
        return convertToBoardResponseDtoList(boardUsers);
    }

    private List<BoardResponseDto> convertToBoardResponseDtoList(List<BoardUser> boardUsers) {
        return boardUsers.stream()
                .map(BoardUser::getBoard)
                .filter(board -> board.getDeletedAt() == null)
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }
}