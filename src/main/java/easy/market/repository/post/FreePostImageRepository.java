package easy.market.repository.post;

import easy.market.entity.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreePostImageRepository extends JpaRepository<PostImage, Long> {
}
