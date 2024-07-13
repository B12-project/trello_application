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
import b12.trello.global.exception.customException.BoardException;
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
        // 보드 생성
        Board board = Board.builder()
                .boardName(boardRequestDto.getBoardName())
                .boardInfo(boardRequestDto.getBoardInfo())
                .manager(user)
                .build();

        boardRepository.save(board);

        // 보드를 생성한 유저를 매니저로 추가
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
        boardUserRepository.verifyBoardUser(board.getId(), user.getId());
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
        validateManager(board, user);

        board.updateBoard(boardRequestDto.getBoardName(), boardRequestDto.getBoardInfo());
        boardRepository.save(board);

        return new BoardResponseDto(board);
    }

    @Transactional
    // TODO: 소프트 딜리트로 변경하기
    public void deleteBoard(Long boardId, User user) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.checkBoardDeleted();
        validateManager(board, user);
//        board.updateDeletedAt();
        boardRepository.deleteById(boardId);
//        boardRepository.delete(board);
    }

    @Transactional
    public void inviteUserByEmail(BoardInviteRequestDto boardInviteRequestDto, User inviter) {
        Board board = boardRepository.findByIdOrElseThrow(boardInviteRequestDto.getBoardId());

        // 매니저만 초대할 수 있는지 확인
        validateManager(board, inviter);

        // 초대할 사용자의 역할 설정
        BoardUser.BoardUserRole role = boardInviteRequestDto.getBoardUserRole();

        // 사용자 이메일로 사용자 찾기
        User foundInvitedUser = userRepository.findByEmailOrElseThrow(boardInviteRequestDto.getUserEmail());

        // 이미 초대된 사용자인지 확인
//        if (boardUserRepository.existsByBoardAndUser(board, foundInvitedUser)) {
//            throw new IllegalArgumentException(USER_ALREADY_INVITED.getErrorDescription());
//        }
        boardUserRepository.verifyNotBoardUser(board.getId(), foundInvitedUser.getId());

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
        List<BoardUser> boardUsers = boardUserRepository.findByUser(user);
        return convertToBoardResponseDtoList(boardUsers);
    }

    // 특정 유저가 매니징 하고 있는 보드 조회
    public List<BoardResponseDto> findBoardListManagedByUser(User user) {
        List<BoardUser> boardUsers = boardUserRepository.findByUserAndBoardUserRole(user, BoardUser.BoardUserRole.MANAGER);
        return convertToBoardResponseDtoList(boardUsers);
    }

    // ColumnService에서 동일하게 사용되고 있는 로직
    // BoardUserRepository에 default Method로 정의하는 방법
    private void validateManager(Board board, User user) {
        BoardUser boardUser = boardUserRepository.findByBoardIdAndUserIdOrElseThrow(board.getId(), user.getId());
        if (boardUser.getBoardUserRole() != BoardUser.BoardUserRole.MANAGER) {
            throw new BoardException(BOARD_MANAGER_ONLY);
        }
    }

    private List<BoardResponseDto> convertToBoardResponseDtoList(List<BoardUser> boardUsers) {
        return boardUsers.stream()
                .map(BoardUser::getBoard)
                .filter(board -> board.getDeletedAt() == null)
                .sorted(Comparator.comparing(Board::getCreatedAt).reversed())
                .map(BoardResponseDto::new)
                .collect(Collectors.toList());
    }

//    private boolean isManager(User user) {
//        return boardUserRepository.existsByUserAndBoardUserRole(user, BoardUser.BoardUserRole.MANAGER);
//    }
//
//    private boolean isBoardMember(Board board, User user) {
//        return boardUserRepository.existsByBoardAndUser(board, user);
//    }
}
