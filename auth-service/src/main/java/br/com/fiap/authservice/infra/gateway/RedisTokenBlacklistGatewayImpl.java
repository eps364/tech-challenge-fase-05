package br.com.fiap.authservice.infra.gateway;

import br.com.fiap.authservice.core.gateway.TokenBlacklistGateway;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisTokenBlacklistGatewayImpl implements TokenBlacklistGateway {
    private final StringRedisTemplate redisTemplate;
    private static final String BLACKLIST_PREFIX = "blacklist:";

    public RedisTokenBlacklistGatewayImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void addToBlacklist(String token, long expiresIn) {
        String key = BLACKLIST_PREFIX + extractJti(token);
        redisTemplate.opsForValue().set(key, "blacklisted", expiresIn, TimeUnit.SECONDS);
    }

    @Override
    public boolean isBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + extractJti(token);
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    private String extractJti(String token) {
        // Extract JTI claim from JWT (simplified - in production use JWT library)
        return token.substring(0, Math.min(20, token.length()));
    }
}
