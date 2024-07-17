package ru.domain.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Логируем выполнения методов с замером времени через аспект
 */
@Aspect
@Component
public class MethodExecutionLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(MethodExecutionLoggingAspect.class);

    @Around("execution(* ru.domain.*.*.*(..))")
    public Object logMethodExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        logger.info("Method {} executed in {} ms", joinPoint.getSignature(), (endTime - startTime));
        return result;
    }
}
