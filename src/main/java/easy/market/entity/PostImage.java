package easy.market.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter @Entity
public class PostImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String imageName;
    private String imageUrl;

    public static PostImage createPostImage(String imageName, String imageUrl) {
        PostImage postImage = new PostImage();
        postImage.imageName = imageName;
        postImage.imageUrl = imageUrl;
        return postImage;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
