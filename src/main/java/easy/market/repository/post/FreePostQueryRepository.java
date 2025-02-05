package easy.market.repository.post;

import easy.market.request.freepost.FreePostListDto;
import easy.market.request.freepost.FreePostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FreePostQueryRepository {
    Page<FreePostListDto> searchPostList(FreePostSearch con, Pageable pageable);
}
