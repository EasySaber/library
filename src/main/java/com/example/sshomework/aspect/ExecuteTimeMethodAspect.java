package com.example.sshomework.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Aleksey Romodin
 *
 * Логирование задержки между получением Request и Response
 */
@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.global} and ${aspect.loggerExecuteTimeMethod}")
public class ExecuteTimeMethodAspect {

    @Around(value = "execution(* @com.example.sshomework.aspect.annotation.LoggerExecuteTime *.*(..))")
    public Object getExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object objectProceed = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;
        log.debug(String.format("Method: %s. Execution time: %d ms",
                joinPoint.getSignature().toShortString(),
                executionTime));
        return objectProceed;
    }
}
