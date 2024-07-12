package b12.trello.domain.user.entity;

import b12.trello.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Length(min = 4, max = 10)
    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserAuth auth;

    @Column(nullable = true)
    private String refreshToken;

    @Builder
    public User(String email, String password, String name, UserAuth auth){
        this.email = email;
        this.password = password;
        this.name = name;
        this.auth = auth;
    }

    public void signOut() {
        updateDeletedAt();
        this.refreshToken = null;
    }

    public void logOut() {
        refreshToken = null;
    }

    public void update(Optional<String> newPassword, Optional<String> name) {
        this.password = newPassword.orElse(this.password);
        this.name = name.orElse(this.name);
    }

    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public enum UserAuth {
        ADMIN(Authority.ADMIN),
        USER(Authority.USER);

        private final String authority;

        UserAuth(String authority) {
            this.authority = authority;
        }

        public String getAuthority() {
            return this.authority;
        }
    }
    public static class Authority {
        public static final String USER = "USER";
        public static final String ADMIN = "ADMIN";
    }
}
