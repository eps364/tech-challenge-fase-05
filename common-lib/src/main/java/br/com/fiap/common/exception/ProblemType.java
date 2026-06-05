/* Copyright (c) 2024 FIAP. All rights reserved. */
package br.com.fiap.common.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.http.HttpStatus;

/**
 * Annotation to map a {@link DomainException} to an RFC 9457 Problem Detail.
 *
 * <p>Applied to exception classes to provide metadata needed to automatically generate consistent
 * Problem Detail responses.
 *
 * <pre>{@code
 * @ProblemType(
 *     type  = "https://api.example.com/problems/triage/not-found",
 *     title = "Triage Not Found",
 *     status = HttpStatus.NOT_FOUND
 * )
 * public class TriageNotFoundException extends DomainException { ... }
 * }</pre>
 *
 * @see DomainException
 * @see GlobalExceptionHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProblemType {

  String type() default "about:blank";

  String title() default "An error occurred";

  HttpStatus status() default HttpStatus.INTERNAL_SERVER_ERROR;

  String description() default "";
}
