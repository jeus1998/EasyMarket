package easy.market.repository.post;

import easy.market.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreePostRepository extends JpaRepository<Post, Long>, FreePostQueryRepository{

}
