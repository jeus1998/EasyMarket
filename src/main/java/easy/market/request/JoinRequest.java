package easy.market.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class JoinRequest {

    @NotNull(message = "아이디는 필수입니다.")
    @NotBlank(message = "공백 문자는 허용되지 않습니다.")
    @Length(min = 4, max = 10, message = "아이디 길이는 최소 4에서 최대 10입니다.")
    private String username;

    @NotNull(message = "비밀번호는 필수입니다.")
    @NotBlank(message = "공백 문자는 허용되지 않습니다.")
    @Length(min = 6, max = 10, message = "비밀번호 길이는 최소 6에서 최대 10입니다.")
    private String password;

    @NotNull(message = "비밀번호는 필수입니다.")
    private String passwordMatch;
}
