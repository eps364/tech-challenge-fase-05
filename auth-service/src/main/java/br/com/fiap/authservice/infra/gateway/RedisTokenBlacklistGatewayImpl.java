/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.authservice.infra.gateway;

import br.com.fiap.authservice.core.gateway.TokenBlacklistGateway;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.concurrent.TimeUnit;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisTokenBlacklistGatewayImpl implements TokenBlacklistGateway {
  private final StringRedisTemplate redisTemplate;
  private static final String BLACKLIST_PREFIX = "blacklist:";

  public RedisTokenBlacklistGatewayImpl(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  public void addToBlacklist(String token, long expiresIn) {
    String key = buildBlacklistKey(token);
    redisTemplate.opsForValue().set(key, "blacklisted", Math.max(expiresIn, 1), TimeUnit.SECONDS);
  }

  @Override
  public boolean isBlacklisted(String token) {
    String key = buildBlacklistKey(token);
    return Boolean.TRUE.equals(redisTemplate.hasKey(key));
  }

  private String buildBlacklistKey(String token) {
    return BLACKLIST_PREFIX + sha256Hex(token);
  }

  private String sha256Hex(String value) {
    try {
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
    } catch (NoSuchAlgorithmException ex) {
      throw new IllegalStateException("SHA-256 algorithm is not available", ex);
    }
  }
}
