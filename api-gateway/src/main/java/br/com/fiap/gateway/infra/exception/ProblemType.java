package br.com.fiap.gateway.infra.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

/**
 * Annotation to map a DomainException to an RFC 9457 Problem Detail.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProblemType {

    String type() default "about:blank";
    String title() default "An error occurred";
    HttpStatus status() default HttpStatus.INTERNAL_SERVER_ERROR;
    String description() default "";
}
