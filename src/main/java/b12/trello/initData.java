//package b12.trello;
//
//import b12.trello.domain.board.entity.Board;
//import b12.trello.domain.boardUser.entity.BoardUser;
//import b12.trello.domain.column.entity.Columns;
//import b12.trello.domain.user.entity.User;
//import jakarta.persistence.EntityManager;
//import jakarta.transaction.Transactional;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.event.ApplicationReadyEvent;
//import org.springframework.context.event.EventListener;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//
//@Component
//@RequiredArgsConstructor
//public class initData {
//    private final EntityManager entityManager;
//
//    @Autowired
//    PasswordEncoder passwordEncoder;
//
//    @EventListener(ApplicationReadyEvent.class)
//    @Transactional
//    public void init() {
//        /**
//         * 유저 저장
//         */
//        User user1 = User.builder()
//                .email("test1@mail.com")
//                .name("test1")
//                .password(passwordEncoder.encode("testPassword!"))
//                .auth(User.UserAuth.USER)
//                .build();
//        User user2 = User.builder()
//                .email("test2@mail.com")
//                .name("test2")
//                .password(passwordEncoder.encode("testPassword!"))
//                .auth(User.UserAuth.USER)
//                .build();
//        User user3 = User.builder()
//                .email("test3@mail.com")
//                .name("test3")
//                .password(passwordEncoder.encode("testPassword!"))
//                .auth(User.UserAuth.USER)
//                .build();
//        save(user1, user2, user3);
////
////        /**
////         * 보드 저장
////         */
////        Board board1 = Board.builder()
////                .boardName("testBoard1")
////                .boardInfo("내용")
////                .build();
////        Board board2 = Board.builder()
////                .boardName("testBoard2")
////                .boardInfo("내용")
////                .build();
////        Board board3 = Board.builder()
////                .boardName("testBoard3")
////                .boardInfo("내용")
////                .build();
////
////        save(board1, board2, board3);
////
////        /**
////         * 보드 사용자 저장
////         */
////        BoardUser boardUser1 = BoardUser.builder()
////                .board(board1)
////                .user(user1)
////                .boardUserRole(BoardUser.BoardUserRole.MANAGER)
////                .build();
////
////        BoardUser boardUser2 = BoardUser.builder()
////                .board(board1)
////                .user(user2)
////                .boardUserRole(BoardUser.BoardUserRole.INVITEE)
////                .build();
////        save(boardUser1, boardUser2);
////
////        /**
////         * 컬럼 저장
////         */
////        Columns column1 = Columns.builder()
////                .columnName("column1")
////                .columnOrder(1L)
////                .board(board1)
////                .build();
////
////        Columns column2 = Columns.builder()
////                .columnName("column2")
////                .columnOrder(2L)
////                .board(board1)
////                .build();
////
////        Columns column3 = Columns.builder()
////                .columnName("column3")
////                .columnOrder(1L)
////                .board(board2)
////                .build();
////
////        Columns column4 = Columns.builder()
////                .columnName("column4")
////                .columnOrder(2L)
////                .board(board2)
////                .build();
////
////        save(column1, column2, column3, column4);
//    }
//
//    public void save(Object... objects) {
//        for (Object object : objects) {
//            entityManager.persist(object);
//        }
//    }
//}
