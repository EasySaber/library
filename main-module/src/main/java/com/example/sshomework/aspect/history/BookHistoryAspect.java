package com.example.sshomework.aspect.history;

import com.example.sshomework.entity.Book;
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
@ConditionalOnExpression("${aspect.global:true} and ${aspect.bookHistory:true}")
public class BookHistoryAspect extends PointcutHistory {

    private final HistoryService historyService;

    @Autowired
    public BookHistoryAspect(@Qualifier("bookHistoryService") HistoryService historyService) {
        this.historyService = historyService;
    }
    /**
     * Получение сущности перед сохранением в БД
     * @param entity Книга
     */
    @Before(value = "(whenSave() || whenDelete()) && args(entity)")
    public void commitEntity(Book entity) {
        historyService.commitEntity(Optional.ofNullable(entity.getId()).orElse(null));
    }

    /**
     * Добавление истории в таблицу "BookHistory" (редактирование/добавление/удаление)
     * @param entity Книга
     */
    @AfterReturning(value = "(whenSave() || whenDelete()) && args(entity)")
    public void addHistory(JoinPoint joinPoint, Book entity) {
        historyService.addNewHistory(entity.getId(), getNameMethod(joinPoint));
    }
}
