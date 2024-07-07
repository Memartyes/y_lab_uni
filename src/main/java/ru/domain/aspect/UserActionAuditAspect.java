package ru.domain.aspect;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Логгируем аудит действий пользователя через аспект.
 */
@Aspect
public class UserActionAuditAspect {
    private static final Logger logger = LoggerFactory.getLogger(UserActionAuditAspect.class);

    @Pointcut("execution(* ru.domain.managers.UserRegistrationManager.*(..))")
    public void userActions() {}

    @AfterReturning(pointcut = "userActions()", returning = "result")
    public void auditUserActions(Object result) {
        logger.info("User action performed with result: {}", result);
    }
}
