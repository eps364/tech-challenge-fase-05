package br.com.fiap.authservice.infra.exception;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.HttpStatus;

/**
 * Annotation to map a DomainException to an RFC 9457 Problem Detail.
 *
 * Applied to exception classes to provide metadata needed to automatically
 * generate consistent Problem Detail responses.
 *
 * Example:
 * <pre>
 * {@code
 * @ProblemType(
 *     type = "https://api.example.com/problems/triage/invalid-risk-level",
 *     title = "Invalid Risk Level",
 *     status = HttpStatus.UNPROCESSABLE_ENTITY
 * )
 * public class InvalidRiskLevelException extends DomainException {
 *     // ...
 * }
 * }
 * </pre>
 *
 * @see DomainException
 * @see GlobalExceptionHandler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ProblemType {

    /**
     * The problem type URI identifying this error category.
     *
     * Must be a valid URI (typically with http or https scheme).
     * Should be under your organization's control.
     *
     * Example: "https://api.example.com/problems/triage/invalid-risk-level"
     */
    String type() default "about:blank";

    /**
     * Short, human-readable summary of the problem type.
     *
     * Should not change between problem occurrences (except for localization).
     *
     * Example: "Invalid Risk Level"
     */
    String title() default "An error occurred";

    /**
     * The HTTP status code to use for this problem.
     *
     * Example: HttpStatus.UNPROCESSABLE_ENTITY (422)
     */
    HttpStatus status() default HttpStatus.INTERNAL_SERVER_ERROR;

    /**
     * Optional description of when/why this problem type occurs.
     * Used for documentation purposes only.
     */
    String description() default "";
}
