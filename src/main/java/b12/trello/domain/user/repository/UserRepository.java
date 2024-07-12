package b12.trello.domain.user.repository;

import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.UserException;
import b12.trello.global.exception.errorCode.UserErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByName(String username);

    default User findByEmailOrElseThrow(String email) {
        return findByEmail(email).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));
    }
}
