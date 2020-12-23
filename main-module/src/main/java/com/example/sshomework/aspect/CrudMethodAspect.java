package com.example.sshomework.aspect;

import com.example.sshomework.aspect.annotation.LoggerCrud;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author Aleksey Romodin
 *
 * Логирование C.R.U.T операций перед выполнением
 * Параметры логирования: Пользователь, метод, операция, аргументы.
 */
@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.global:true} and ${aspect.loggerCrudMethod:true}")
public class CrudMethodAspect {

    @Before(value = "@annotation(com.example.sshomework.aspect.annotation.LoggerCrud)")
    public void getOperationParameters (JoinPoint joinPoint) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        LoggerCrud loggerCrud = ((MethodSignature)joinPoint.getSignature()).getMethod().getAnnotation(LoggerCrud.class);
        log.info(String.format("%nUser: %s.%nCause method: %s.%nOperation: %s.%nArguments: %s.",
                auth.getName(),
                joinPoint.getSignature().toShortString(),
                loggerCrud.operation(),
                Arrays.toString(joinPoint.getArgs())));
    }
}
