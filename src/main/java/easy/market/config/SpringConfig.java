package easy.market.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.Optional;

@Configuration
public class SpringConfig {

    @Bean
    public JPAQueryFactory jpaQueryFactory(EntityManager em) {
        return new JPAQueryFactory(em);
    }

    @Bean
    @Profile("develop")
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.of(SecurityContextHolder.getContextHolderStrategy()
                .getContext().getAuthentication().getName());
    }
}
