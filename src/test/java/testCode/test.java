package testCode;

import b12.trello.domain.user.entity.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class test {

    private static final Logger log = LoggerFactory.getLogger(test.class);

    @Test
    @DisplayName("실험쥐")
    public void testingNow() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        log.info("what");
        var test = new test();
        List<String> passwordHistory = Arrays.asList("oldpassword1", "oldpassword2");
        assertThat(test).isNotNull();

        User user = User.class.getDeclaredConstructor().newInstance();
    }

}
