package b12.trello.domain.user.service;

import b12.trello.domain.user.dto.*;
import b12.trello.domain.user.entity.User;
import b12.trello.domain.user.repository.UserRepository;
import b12.trello.global.exception.customException.UserException;
import b12.trello.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

import static org.hibernate.query.sqm.tree.SqmNode.log;


@Service
@RequiredArgsConstructor
public class UserService {

    private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public SignupResponseDto signUp(SignupRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new UserException(UserErrorCode.EMAIL_DUPLICATED);
        }
        String encodedPassword = passwordEncoder.encode((signupRequestDto.getPassword()));

        User.UserAuth auth = User.UserAuth.USER;

        if (!(signupRequestDto.getAdminToken() == null)){
            if (ADMIN_TOKEN.equals(signupRequestDto.getAdminToken())) {
                auth = User.UserAuth.ADMIN;
            }
            else {
                throw new UserException(UserErrorCode.INVALID_ADMIN_TOKEN);
            }
        }

        User newUser = new User(signupRequestDto.getEmail(),
                encodedPassword,
                signupRequestDto.getName(),
                auth
        );

        userRepository.save(newUser);

        return SignupResponseDto.of(newUser);
    }

    public SignupResponseDto signOut(User user) {
        SignupResponseDto signupResponseDto = SignupResponseDto.of(user);
//        user.signOut();
//        userRepository.save(user);
        user.resetRefreshToken();
        userRepository.save(user);
        userRepository.deleteById(user.getId());
        return signupResponseDto;
    }

    public SignupResponseDto logOut(User user) {
        SignupResponseDto signupResponseDto = SignupResponseDto.of(user);
        user.resetRefreshToken();
        userRepository.save(user);
        return signupResponseDto;
    }


    public ProfileResponseDto getProfile(User user) {
        return ProfileResponseDto.of(user);
    }

    public ProfileResponseDto updateProfile(User user, ProfileRequestDto requestDto) {
        user.updateProfile(requestDto.getName());
        userRepository.save(user);
        return ProfileResponseDto.of(user);
    }


    public void updatePassword(User user, String currentPassword, String newPassword) {


//        log.info(user.getName());
//        log.info(currentPassword);
//        log.info(newPassword);

        if (currentPassword == null || newPassword == null) {
            throw new IllegalArgumentException("Passwords cannot be null");
        }

        // 현재 비밀번호가 일치하는지 확인
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }

        // 새로운 비밀번호가 동일한지 확인
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new UserException(UserErrorCode.PASSWORD_DUPLICATED);
        }
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.updatePassword(Optional.ofNullable(encodedPassword));
        userRepository.save(user);
    }
}
