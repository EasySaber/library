package com.example.sshomework.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Aleksey Romodin
 *
 * Логирование перед выполнением методов без аргументов
 */
@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.global:true} and ${aspect.loggerNoArgsMethod:true}")
public class NotArgsMethodAspect {

    @Before(value = "within(@org.springframework.stereotype.Service *) && args()")
    public void getVoidOperationParameters(JoinPoint joinPoint) {
        log.info(String.format("Method: %s. Arguments: %s.",
                joinPoint.getSignature().toShortString(),
                Arrays.toString(joinPoint.getArgs())));
    }
}
