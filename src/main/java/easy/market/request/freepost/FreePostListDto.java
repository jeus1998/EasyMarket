package easy.market.request.freepost;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter
@ToString
public class FreePostListDto {
    private Long postId;
    private String title;
    private String username;
    private int likeCount;
    private int viewCount;
    private long commentCount;
    private LocalDateTime createdAt;
}
