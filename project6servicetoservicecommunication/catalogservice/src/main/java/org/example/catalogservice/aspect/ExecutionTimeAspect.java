package org.example.catalogservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExecutionTimeAspect {

    @Around("execution(* org.example.catalogservice.category.service..*(..)) || execution(* org.example.catalogservice.product.service..*(..))")
         //("execution(returnType PackageName inThatPackageAndSubPackage Methods (MethodArguments))")
    public Object monitorExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long startTime = System.nanoTime();

        try {
            return joinPoint.proceed();
        } finally {

            long executionTime = (System.nanoTime() - startTime) / 1_000_000;

            log.info("Method: {} | Execution Time: {} ms", joinPoint.getSignature().toShortString(), executionTime);
        }
    }

}
