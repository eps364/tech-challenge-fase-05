/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.gateway.infra.handler;

import br.com.fiap.gateway.infra.exception.ProblemDetailBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.net.ConnectException;
import java.time.Instant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

/**
 * Reactive WebExceptionHandler to handle gateway-level errors (e.g. upstream service unavailable).
 * Runs at order -2 to intercept before the default Spring error handler.
 */
@Component
@Order(-2)
public class GlobalWebExceptionHandler implements WebExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(GlobalWebExceptionHandler.class);

  private final ObjectMapper objectMapper =
      new ObjectMapper()
          .registerModule(new JavaTimeModule())
          .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

  @Override
  @SuppressWarnings("null")
  public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
    HttpStatus status = resolveStatus(ex);
    if (status == null) {
      return Mono.error(ex);
    }

    ServerHttpResponse response = exchange.getResponse();
    if (response.isCommitted()) {
      return Mono.error(ex);
    }

    log.warn(
        "Gateway upstream error [{}] on {}: {}",
        status.value(),
        exchange.getRequest().getURI().getPath(),
        ex.getMessage());

    response.setStatusCode(status);
    response.getHeaders().setContentType(MediaType.parseMediaType("application/problem+json"));

    String requestPath = exchange.getRequest().getURI().getPath();
    var problem =
        new ProblemDetailBuilder()
            .type("urn:problem:api-gateway:service-unavailable")
            .status(status.value())
            .title(status.getReasonPhrase())
            .detail("The requested service is temporarily unavailable. Please try again later.")
            .instance(requestPath)
            .timestamp(Instant.now())
            .build();

    return Mono.fromCallable(() -> objectMapper.writeValueAsBytes(problem))
        .flatMap(
            bytes -> {
              response.getHeaders().setContentLength(bytes.length);
              var buf = response.bufferFactory().wrap(bytes);
              return response.writeWith(Mono.just(buf));
            });
  }

  private HttpStatus resolveStatus(Throwable ex) {
    if (ex instanceof ConnectException) {
      return HttpStatus.SERVICE_UNAVAILABLE;
    }
    if (ex.getCause() instanceof ConnectException) {
      return HttpStatus.SERVICE_UNAVAILABLE;
    }
    if (ex instanceof ResponseStatusException rse
        && rse.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
      return HttpStatus.SERVICE_UNAVAILABLE;
    }
    return null;
  }
}
