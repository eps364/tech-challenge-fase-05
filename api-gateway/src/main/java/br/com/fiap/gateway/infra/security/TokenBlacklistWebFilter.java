/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.gateway.infra.security;

import br.com.fiap.gateway.infra.exception.ProblemDetail;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HexFormat;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TokenBlacklistWebFilter implements WebFilter {

  private static final String BLACKLIST_PREFIX = "blacklist:";
  private static final String BEARER_PREFIX = "Bearer ";

  private final StringRedisTemplate redisTemplate;
  private final ObjectMapper objectMapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  public TokenBlacklistWebFilter(StringRedisTemplate redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  @Override
  @SuppressWarnings("null")
  public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
    String token = extractBearerToken(exchange);
    if (token == null) {
      return chain.filter(exchange);
    }

    return Mono.fromCallable(
            () -> Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + sha256Hex(token))))
        .subscribeOn(Schedulers.boundedElastic())
        .flatMap(blacklisted -> blacklisted ? writeUnauthorized(exchange) : chain.filter(exchange));
  }

  private String extractBearerToken(ServerWebExchange exchange) {
    String authorization = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
    if (authorization == null || !authorization.startsWith(BEARER_PREFIX)) {
      return null;
    }
    return authorization.substring(BEARER_PREFIX.length());
  }

  private Mono<Void> writeUnauthorized(ServerWebExchange exchange) {
    ServerHttpResponse response = exchange.getResponse();
    response.setStatusCode(HttpStatus.UNAUTHORIZED);
    response.getHeaders().setContentType(MediaType.parseMediaType("application/problem+json"));

    ProblemDetail problem = new ProblemDetail();
    problem.setType("urn:problem:api-gateway:token-revoked");
    problem.setStatus(HttpStatus.UNAUTHORIZED.value());
    problem.setTitle("Unauthorized");
    problem.setDetail("JWT token has been revoked");
    problem.setInstance(exchange.getRequest().getURI().getPath());
    problem.setTimestamp(Instant.now());

    return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(problem))
        .flatMap(
            bytes -> {
              response.getHeaders().setContentLength(bytes.length);
              var buf = response.bufferFactory().wrap(bytes);
              return response.writeWith(Mono.just(buf));
            });
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
