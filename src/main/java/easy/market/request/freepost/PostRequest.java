package easy.market.request.freepost;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.List;

@ToString(of = {"title", "content", "images"})
@Getter
@NoArgsConstructor
public class PostRequest {
    @NotBlank(message = "제목은 필수입니다.")
    private String title;

    private String content;

    @Size(max = 3, message = "이미지 최대 개수는 3개입니다.")
    private List<ImageData> images;

    @Getter
    @NoArgsConstructor
    @ToString(of = "fileName")
    public static class ImageData{
        private String fileName;
        private String base64Data;
    }
}
