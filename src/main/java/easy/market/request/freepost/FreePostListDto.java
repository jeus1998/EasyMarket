package easy.market.request.freepost;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class FreePostListDto {
    private String postId;
    private String title;
    private String username;
    private int likeCount;
    private int viewCount;
    private int commentCount;
}
