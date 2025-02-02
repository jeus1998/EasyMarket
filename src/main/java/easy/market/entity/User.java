package easy.market.entity;

import easy.market.request.JoinRequest;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
public class User extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;

    private String role;

    public User(String username) {
        this.username = username;
    }

    public static User joinUser(JoinRequest joinRequest){
        User user = new User();
        user.setUsername(joinRequest.getUsername());
        user.setPassword(joinRequest.getPassword());
        user.setNickName(UUID.randomUUID().toString());
        return user;
    }

    private void setNickName(String nickname) {
        this.nickname = nickname;
    }

    private void setUsername(String username) {
        this.username = username;
    }
    private void setPassword(String password) {
        this.password = password;
    }

}
