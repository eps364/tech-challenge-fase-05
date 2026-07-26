/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.susconnect.aps.infra.web.exception;

import br.com.fiap.susconnect.aps.core.domain.ApsValidationException;
import br.com.fiap.susconnect.aps.core.domain.SearchActionNotFoundException;
import br.com.fiap.susconnect.aps.core.domain.TerritoryAlreadyExistsException;
import br.com.fiap.susconnect.aps.core.domain.TerritoryNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ApsExceptionHandler {

  private static final URI VALIDATION_ERROR =
      URI.create("https://api.example.com/problems/aps-prioritization/validation-error");
  private static final URI TERRITORY_NOT_FOUND =
      URI.create("https://api.example.com/problems/aps-prioritization/territory-not-found");
  private static final URI ACTION_NOT_FOUND =
      URI.create("https://api.example.com/problems/aps-prioritization/search-action-not-found");
  private static final URI TERRITORY_CONFLICT =
      URI.create("https://api.example.com/problems/aps-prioritization/territory-conflict");
  private static final URI BAD_REQUEST = URI.create("about:blank");

  @ExceptionHandler(ApsValidationException.class)
  public ResponseEntity<ProblemDetail> handleDomainValidation(
      ApsValidationException exception, HttpServletRequest request) {
    return response(
        HttpStatus.UNPROCESSABLE_ENTITY,
        VALIDATION_ERROR,
        "Invalid APS prioritization data",
        exception.getMessage(),
        request);
  }

  @ExceptionHandler({TerritoryNotFoundException.class, SearchActionNotFoundException.class})
  public ResponseEntity<ProblemDetail> handleNotFound(
      RuntimeException exception, HttpServletRequest request) {
    if (exception instanceof SearchActionNotFoundException) {
      return response(
          HttpStatus.NOT_FOUND,
          ACTION_NOT_FOUND,
          "Search action not found",
          exception.getMessage(),
          request);
    }
    return response(
        HttpStatus.NOT_FOUND,
        TERRITORY_NOT_FOUND,
        "Territory not found",
        exception.getMessage(),
        request);
  }

  @ExceptionHandler(TerritoryAlreadyExistsException.class)
  public ResponseEntity<ProblemDetail> handleConflict(
      TerritoryAlreadyExistsException exception, HttpServletRequest request) {
    return response(
        HttpStatus.CONFLICT,
        TERRITORY_CONFLICT,
        "Territory already exists",
        exception.getMessage(),
        request);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ProblemDetail> handleRequestValidation(
      MethodArgumentNotValidException exception, HttpServletRequest request) {
    Map<String, String> errors = new LinkedHashMap<>();
    for (FieldError error : exception.getBindingResult().getFieldErrors()) {
      errors.putIfAbsent(error.getField(), error.getDefaultMessage());
    }
    ProblemDetail problem =
        problem(
            HttpStatus.UNPROCESSABLE_ENTITY,
            VALIDATION_ERROR,
            "Invalid APS prioritization request",
            "One or more fields failed validation",
            request);
    problem.setProperty("errors", errors);
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problem);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ProblemDetail> handleIllegalArgument(
      IllegalArgumentException exception, HttpServletRequest request) {
    return response(HttpStatus.BAD_REQUEST, BAD_REQUEST, "Bad request", exception.getMessage(), request);
  }

  private ResponseEntity<ProblemDetail> response(
      HttpStatus status, URI type, String title, String detail, HttpServletRequest request) {
    return ResponseEntity.status(status)
        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
        .body(problem(status, type, title, detail, request));
  }

  private ProblemDetail problem(
      HttpStatus status, URI type, String title, String detail, HttpServletRequest request) {
    ProblemDetail problem = ProblemDetail.forStatusAndDetail(status, detail);
    problem.setType(type);
    problem.setTitle(title);
    problem.setInstance(URI.create(request.getRequestURI()));
    return problem;
  }
}
