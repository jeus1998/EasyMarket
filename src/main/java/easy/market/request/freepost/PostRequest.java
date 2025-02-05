package easy.market.request.freepost;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter @Setter
public class PostRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private List<PostItem> items;
}
