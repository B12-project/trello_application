package b12.trello.domain.user.service;

import b12.trello.domain.user.dto.SignupRequestDto;
import b12.trello.domain.user.dto.SignupResponseDto;
import b12.trello.domain.user.entity.User;
import b12.trello.domain.user.repository.UserRepository;
import b12.trello.global.exception.customException.UserException;
import b12.trello.global.exception.errorCode.UserErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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

        if (!signupRequestDto.getAdminToken().isEmpty()){
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
}
