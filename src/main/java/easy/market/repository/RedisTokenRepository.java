package easy.market.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisTokenRepository {
    private final RedisTemplate<String, String> redisTemplate;
    private static final long REFRESH_TOKEN_EXPIRATION = 24 * 60 * 60;

    public void saveRefreshToken(String username, String refreshToken) {
        redisTemplate
                .opsForValue()
                .set(username, refreshToken, REFRESH_TOKEN_EXPIRATION, TimeUnit.SECONDS);
    }
    public String getRefreshToken(String username) {
        return redisTemplate.opsForValue().get(username);
    }
    public void removeRefreshToken(String username) {
        redisTemplate.delete(username);
    }

    public void flushDb(){
        redisTemplate.getConnectionFactory().getConnection().flushDb();
    }
}
