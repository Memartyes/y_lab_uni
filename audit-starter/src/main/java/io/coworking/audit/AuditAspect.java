package io.coworking.audit;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuditAspect {
    private static final Logger logger = LoggerFactory.getLogger(AuditAspect.class);

    @Pointcut("execution(* io.coworking.*.*.*(..))")
    public void domainActions() {}

    @AfterReturning(pointcut = "domainActions()", returning = "result")
    public void auditActions(Object result) {
        logger.info("User action performed with result: {}", result);
    }
}
