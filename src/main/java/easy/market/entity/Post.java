package easy.market.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    private String content;

    private Long viewCount;

    private Long likeCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "post")
    private List<PostComment> comments = new ArrayList<>();

    public static Post createPost(String title, String content, List<PostImage> postImages) {
        Post post = new Post();
        post.title = title;
        post.content = content;
        for (PostImage postImage : postImages) {
            post.addImage(postImage);
            postImage.setPost(post);
        }
        return post;
    }
    private void addImage(PostImage postImage) {
        images.add(postImage);
    }
}
