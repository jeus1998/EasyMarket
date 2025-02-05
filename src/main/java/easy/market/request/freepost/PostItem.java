package easy.market.request.freepost;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.web.multipart.MultipartFile;

@Getter @Setter
@ToString
public class PostItem {
    private int order;
    private String type;
    private String content;
    private MultipartFile imageFile;
}
