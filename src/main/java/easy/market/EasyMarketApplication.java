package easy.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class EasyMarketApplication {

    public static void main(String[] args) {
        SpringApplication.run(EasyMarketApplication.class, args);
    }

}
