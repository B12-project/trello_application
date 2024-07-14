package b12.trello.test;

import b12.trello.domain.board.entity.Board;
import b12.trello.domain.board.repository.BoardRepository;
import b12.trello.domain.boardUser.entity.BoardUser;
import b12.trello.domain.boardUser.repository.BoardUserRepository;
import b12.trello.domain.column.dto.ColumnCreateRequestDto;
import b12.trello.domain.column.repository.ColumnRepository;
import b12.trello.domain.column.service.ColumnService;
import b12.trello.domain.user.repository.UserRepository;
import java.util.stream.IntStream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.BDDMockito.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ConcurrencyTest implements CommonTest {


    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ColumnRepository columnRepository;
    @Autowired
    private BoardUserRepository boardUserRepository;


    @Autowired
    private ColumnService columnService;

    @Mock
    ColumnCreateRequestDto requestDto;

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    class ConcurrencyTest1 {


        @Order(1)
        @Test
        public void setUp() {

            userRepository.save(TEST_USER);

            Board board = Board.builder()
                .boardName("test1")
                .id(1L)
                .build();
            boardRepository.save(board);

            BoardUser boardUser = BoardUser.builder()
                .board(board)
                .user(TEST_USER)
                .boardUserRole(BoardUser.BoardUserRole.MANAGER)
                .build();

            boardUserRepository.save(boardUser);


        }

        @Order(2)
        @DisplayName("컬럼생성")
        @Test
        void columnTest() {

            IntStream.range(0, 150)
                .parallel()
                .forEach((x) -> {
                    try {
                        //Thread.sleep(x*1000);
                        test(x);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
        }

        @Order(3)
        @DisplayName("생성컬럼 순서확인")
        @Test
        void afterTet() {
            columnRepository.findAll().stream()
                .forEach(x -> System.out.println(x.getColumnName() + ": " + x.getColumnOrder()));
        }


    }

    void test(int x) {
        ColumnCreateRequestDto testRequestDto = new ColumnCreateRequestDto();
        testRequestDto.setColumnName("test" + x);
        testRequestDto.setBoardId(1L);

        columnService.createColumn(TEST_USER, testRequestDto);
    }

}
