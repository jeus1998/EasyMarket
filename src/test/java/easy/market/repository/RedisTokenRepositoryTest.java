package easy.market.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class RedisTokenRepositoryTest {

    @Autowired
    private RedisTokenRepository redisTokenRepository;

    @BeforeEach
    void setUp() {
        redisTokenRepository.flushDb();
    }

    @Test
    public void saveRefreshToken(){
        // given
        String username = "jeu";
        String refreshToken = "123456";

        // when
        redisTokenRepository.saveRefreshToken(username, refreshToken);

        // then
        String findToken = redisTokenRepository.getRefreshToken(username);
        assertThat(findToken).isEqualTo(refreshToken);
    }

    @Test
    public void getRefreshToken(){
        // given
        String username = "jeu";
        String refreshToken = "123456";
        redisTokenRepository.saveRefreshToken(username, refreshToken);

        // when
        redisTokenRepository.removeRefreshToken(username);

        // then
        String findToken = redisTokenRepository.getRefreshToken(username);
        assertThat(findToken).isNull();
    }


}