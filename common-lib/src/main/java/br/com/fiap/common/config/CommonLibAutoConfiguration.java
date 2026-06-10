package br.com.fiap.common.config;

import br.com.fiap.common.exception.ExceptionToProblemMapper;
import br.com.fiap.common.exception.GlobalExceptionHandler;
import br.com.fiap.common.exception.TraceIdProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

/**
 * Spring Boot autoconfiguration for common-lib.
 *
 * <p>Automatically registers {@link TraceIdProvider}, {@link ExceptionToProblemMapper} and {@link
 * GlobalExceptionHandler} as Spring beans in any service that depends on common-lib, regardless of
 * that service's component scan base packages.
 */
@AutoConfiguration
@Import({TraceIdProvider.class, ExceptionToProblemMapper.class, GlobalExceptionHandler.class})
public class CommonLibAutoConfiguration {}
