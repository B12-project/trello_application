package b12.trello.test;

import b12.trello.domain.user.entity.User;
import b12.trello.domain.user.entity.User.UserAuth;

public interface CommonTest {

    String TEST_USER_NAME = "username";
    String TEST_USER_PASSWORD = "password";
    String TEST_USER_EMAIL = "user@email.com";
    User TEST_USER = User.builder()
        .name(TEST_USER_NAME)
        .password(TEST_USER_PASSWORD)
        .email(TEST_USER_EMAIL)
        .auth(UserAuth.USER)
        .build();
}
