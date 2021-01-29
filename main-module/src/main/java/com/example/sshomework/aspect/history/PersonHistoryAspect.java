package com.example.sshomework.aspect.history;

import com.example.sshomework.entity.Person;
import com.example.sshomework.service.history.HistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * @author Aleksey Romodin
 */
@Aspect
@Component
@ConditionalOnExpression("${aspect.global:true} and ${aspect.personHistory:true}")
public class PersonHistoryAspect extends PointcutHistory{

    private final HistoryService historyService;

    public PersonHistoryAspect(@Qualifier("personHistoryService")HistoryService historyService) {
        this.historyService = historyService;
    }

    /**
     * Получение сущности перед сохранением в БД
     * @param entity Сущность
     */
    @Before(value = "(whenSave() || whenDelete()) && args(entity)")
    public void commitEntity(Person entity) {
        historyService.commitEntity(Optional.ofNullable(entity.getId()).orElse(null));
    }

    /**
     * Добавление истории в таблицу "History" (редактирование/добавление/удаление)
     * @param entity Сущность
     */
    @AfterReturning(value = "(whenSave() || whenDelete()) && args(entity)")
    public void addHistory(JoinPoint joinPoint, Person entity) {
        historyService.addNewHistory(entity.getId(), getNameMethod(joinPoint));
    }

    /**
     * Добавление истории в таблицу "History" (редактирование/добавление/удаление)
     * Удаление списком
     * @param entityList список сущностей
     */
    @AfterReturning(value = "whenDeleteAll() && args(entityList)")
    public void addHistoryDeleteAll(JoinPoint joinPoint, List<Person> entityList) {
        entityList.forEach(entity -> historyService.addNewHistory(entity.getId(), getNameMethod(joinPoint)));
    }

}
