package com.example.sshomework.aspect.history;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Aleksey Romodin
 */
public class PointcutHistory {

    @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.save(*))")
    protected void whenSave() {
    }

    @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.delete(*))")
    protected void whenDelete() {
    }

    @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.deleteAll(*))")
    protected void whenDeleteAll() {
    }

    protected String getNameMethod(JoinPoint joinPoint) {
        return joinPoint.getSignature().getName();
    }
}
