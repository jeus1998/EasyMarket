package easy.market.controller.post;

import easy.market.request.freepost.PostRequest;
import easy.market.service.FreePostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/free/posts")
@RequiredArgsConstructor
@Slf4j
public class FreePostApiController {
     private final FreePostService freePostService;
     @PostMapping
     public ResponseEntity<Long> post(@RequestBody PostRequest request) {
         log.info("Post request: {}", request);
         Long id = freePostService.addPost(request);
         return ResponseEntity.ok(id);
     }
}
