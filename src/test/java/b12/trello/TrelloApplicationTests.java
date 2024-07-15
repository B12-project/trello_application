package b12.trello;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TrelloApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(TrelloApplicationTests.class);

    @Test
    void contextLoads() {
        log.info("테스트");
    }

}
