package com.example.sshomework.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

/**
 * @author Aleksey Romodin
 *
 * Логирование возникающих исключений при выполнении метода
 * Параметры логирования: метод, исключение
 */
@Aspect
@Component
@Slf4j
@ConditionalOnExpression("${aspect.global} and ${aspect.loggerExceptionCatch}")
public class ExceptionCatchAspect {

    @AfterThrowing(value =
            "execution(* *..AuthorService.*(..)) || " +
            "execution(* *..BookService.*(..)) || " +
            "execution(* *..GenreService.*(..)) || " +
            "execution(* *..LibraryCard.*(..)) || " +
            "execution(* *..PersonService.*(..)) || " +
            "execution(* *..UserService.*(..))",
            throwing = "ex")
    public void getMethodException(JoinPoint joinPoint, Exception ex) {
        log.error(String.format("Method: %s. Exception: '%s'",
                joinPoint.getSignature().toShortString(),
                ex.getMessage()));
    }
}
