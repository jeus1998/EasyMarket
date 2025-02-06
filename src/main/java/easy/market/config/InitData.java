package easy.market.config;

import easy.market.entity.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.Collections;

@RequiredArgsConstructor
public class InitData {
    private final EntityManager em;
    @Transactional
    @PostMapping
    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        for (int i = 1; i <= 120; i++) {
            em.persist(Post.createPost(
                    String.valueOf(i),
                    String.valueOf(i),
                    Collections.emptyList()));
        }
    }
}
