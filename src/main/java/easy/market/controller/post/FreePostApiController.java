package easy.market.controller.post;

import easy.market.repository.post.FreePostQueryRepository;
import easy.market.repository.post.FreePostRepository;
import easy.market.request.freepost.FreePostListDto;
import easy.market.request.freepost.FreePostSearch;
import easy.market.request.freepost.PostRequest;
import easy.market.request.freepost.SortBy;
import easy.market.service.FreePostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/free/posts")
@RequiredArgsConstructor
@Slf4j
public class FreePostApiController {

    private final FreePostService freePostService;
    private final FreePostRepository freePostRepository;

    @PostMapping
    public ResponseEntity<Long> addPost(@Valid @RequestBody PostRequest request) {
        Long id = freePostService.addPost(request);
        return ResponseEntity.ok(id);
    }

    @GetMapping
    public Page<FreePostListDto> getPosts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String createdBy,
            @RequestParam(required = false) SortBy sortBy,
            @PageableDefault(size = 10, page = 0) Pageable pageable) {

        FreePostSearch searchCon = FreePostSearch.builder()
                .title(title)
                .createdBy(createdBy)
                .sortBy(sortBy)
                .build();

        return freePostRepository.searchPostList(searchCon, pageable);
    }
}