package b12.trello.domain.user.entity;

import b12.trello.global.entity.TimeStampedWithDeletedAt;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.validator.constraints.Length;

import java.util.Optional;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP where id = ?")
public class User extends TimeStampedWithDeletedAt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Length(min = 1, max = 10)
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

    public void resetRefreshToken() {
        this.refreshToken = null;
    }

    public void logOut() {
        this.refreshToken = null;
    }

    public void updatePassword(Optional<String> newPassword) {
        this.password = newPassword.orElse(this.password);
    }

    public void updateProfile(String email, String name) {
        this.email = email;
        this.name = name;
    }


    public void updateToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public enum UserAuth {
        ADMIN,
        USER;
    }

}
