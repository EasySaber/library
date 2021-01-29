package com.example.sshomework.aspect.history;

import com.example.sshomework.entity.Author;
import com.example.sshomework.service.history.HistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@Aspect
@Component
@ConditionalOnExpression("${aspect.global:true} and ${aspect.personHistory:true}")
public class AuthorHistoryAspect extends PointcutHistory {

    private final HistoryService historyService;

    @Autowired
    public AuthorHistoryAspect(@Qualifier("authorHistoryService") HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Получение сущности перед сохранением в БД
     * @param entity Автор
     */
    @Before(value = "(whenSave() || whenDelete()) && args(entity)")
    public void commitEntity(Author entity) {
        historyService.commitEntity(Optional.ofNullable(entity.getId()).orElse(null));
    }

    /**
     * Добавление истории в таблицу "AuthorHistory" (редактирование/добавление/удаление)
     * @param entity Автор
     */
    @AfterReturning(value = "(whenSave() || whenDelete()) && args(entity)")
    public void addHistory(JoinPoint joinPoint, Author entity) {
        historyService.addNewHistory(entity.getId(), getNameMethod(joinPoint));
    }

}
