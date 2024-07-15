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
// 조회 시 @Where를 사용하면 소프트 딜리트 된 걸 제외하고 조회 할 수 있음
@SQLDelete(sql = "UPDATE users SET deleted_at = CURRENT_TIMESTAMP where id = ?") //삭제가 들어왔을 때 업데이트 문을 대신 나가도록
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

    public void resetRefreshToken() {
        this.refreshToken = null;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updatePassword(Optional<String> newPassword) {
        this.password = newPassword.orElse(this.password);
    }

    public void updateProfile(String name) {
        this.name = name;
    }

    public enum UserAuth {
        ADMIN,
        USER;
    }

}
