package b12.trello.domain.user.repository;

import b12.trello.domain.user.entity.User;
import b12.trello.global.exception.customException.UserException;
import b12.trello.global.exception.errorCode.UserErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
    boolean existsUserByIdAndDeletedAtIsNull(Long userId);
    Optional<User> findByName(String username);

    List<User> findAllByDeletedAtIsNull();

    default User findByEmailOrElseThrow(String email) {
        return findByEmailAndDeletedAtIsNull(email).orElseThrow(() ->
                new UserException(UserErrorCode.USER_NOT_FOUND));
    }

    default void verifyUserStatus(Long userId) {
        if(!existsUserByIdAndDeletedAtIsNull(userId)) {
            throw new UserException(UserErrorCode.SIGN_OUT_USER);
        }
    }
}
