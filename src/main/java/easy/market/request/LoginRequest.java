package easy.market.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter
@ToString
public class LoginRequest {
    private String username;
    private String password;
}
