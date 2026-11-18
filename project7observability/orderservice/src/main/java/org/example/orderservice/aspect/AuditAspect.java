package org.example.orderservice.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AuditAspect {
    @AfterReturning("@annotation(auditable)")
    public void auditAction(JoinPoint joinPoint, Auditable auditable) {
        log.info("AUDIT | Action: {} | Method: {} | Status: SUCCESS", auditable.action(), joinPoint.getSignature().toShortString());
    }
}
