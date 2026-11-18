package org.example.catalogservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionLoggingAspect {
    @AfterThrowing(pointcut = "execution(* org.example.catalogservice.category.service..*(..)) || execution(* org.example.catalogservice.product.service..*(..))" , throwing = "exception")
    public void logException(JoinPoint joinPoint, Throwable exception) {
        log.error("Exception in method: {} | Error: {}", joinPoint.getSignature().toShortString(), exception.getMessage(), exception);
    }
}
