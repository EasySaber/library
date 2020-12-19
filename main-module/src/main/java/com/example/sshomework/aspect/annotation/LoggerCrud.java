package com.example.sshomework.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Aleksey Romodin
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LoggerCrud {

    Operation operation();

    enum Operation {
        CREATE,
        UPDATE,
        DELETE
    }
}
