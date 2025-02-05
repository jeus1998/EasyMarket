package easy.market.service;

import easy.market.repository.post.FreePostImageRepository;
import easy.market.repository.post.FreePostRepository;
import easy.market.request.freepost.PostRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FreePostService {
    private final FreePostRepository freePostRepository;
    private final FreePostImageRepository freePostImageRepository;

    public Long addPost(PostRequest postRequest) {
        return 1L;
    }
}
