package b12.trello.domain.user.entity;

import b12.trello.global.entity.TimeStamped;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name="users")
@NoArgsConstructor
public class User extends TimeStamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    //Enum 으로 관리?
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

    public enum UserAuth {
        ADMIN,
        USER,
        DELETED
    }
}
