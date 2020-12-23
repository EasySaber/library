package com.example.sshomework.service.history.impl;

import com.example.sshomework.entity.MainEntity;
import com.example.sshomework.entity.history.OperationHistory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;
import java.util.Set;

/**
 * Класс-родитель с общими методами для сервисного слоя Историй
 * @author Aleksey Romodin
 */
public class ParentHistoryService {

    /**
     * Сравнение объектов для формирования Map
     * @param differenceMap Map с новыми значениями
     * @param beforeField Поле сущности из БД перед сохранением
     * @param afterField Поле сущности из БД после сохранением
     * @param nameField Имя переменной
     */
    protected void putMapDifference(Map<String, Object> differenceMap, Object beforeField, Object afterField, String nameField) {
        if ((beforeField != null) && (!beforeField.equals(afterField))) {
            differenceMap.put(nameField, afterField);
        }
    }

    /**
     * Получение Username из контекста
     * @return Username
     */
    protected String getUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    /**
     * Определение характера операции над сущностью CREATE/UPDATE/DELETE
     * @param method Перехваченный метод
     * @param afterEntity Сущность после сохранения в БД
     * @return Вид операции
     */
    protected OperationHistory getOperation(String method, MainEntity afterEntity) {
        OperationHistory operation = OperationHistory.DELETE;
        if ((method.equals("save")) && (afterEntity != null)) {
            Long version = afterEntity.getVersion();
            operation = (version == 0) ? OperationHistory.CREATE : OperationHistory.UPDATE;
        }
        return operation;
    }

    /**
     * Конвертация сущности в Map
     * @param objectMapper Маппер с параметрами
     * @param entity Обьект для маппинга
     * @param operation Характеристика операции
     * @param status Статус сущности до/после изменения
     * @return Map сущности для сохранения в БД
     */
    protected Map<String, Object> getMapFromEntity(ObjectMapper objectMapper, Object entity, OperationHistory operation, String status) {

        if (status.equals("before")) {
            return (operation == OperationHistory.CREATE) ? null : getMapFromObject(objectMapper, entity);
        }
        if (status.equals("after")) {
            return (operation == OperationHistory.DELETE) ? null : getMapFromObject(objectMapper, entity);
        }
        return null;
    }

    /**
     * Конвертация Set<Object> в Set<Map> + сериализация вложенных сущностей
     * @param objectMapper Маппер с параметрами
     * @param object Обьект для маппинга
     * @return Set<Map>
     */
    protected Set<Map<String, Object>> getSetMapFromSetObject(ObjectMapper objectMapper, Object object) {
        return objectMapper.convertValue(object, new TypeReference<Set<Map<String, Object>>>() {});
    }

    /**
     * Конвертация обьекта в Map
     * @param objectMapper Маппер с параметрами
     * @param object Обьект для маппинга
     * @return Map объекта
     */
    protected Map<String, Object> getMapFromObject(ObjectMapper objectMapper, Object object) {
        return objectMapper.convertValue(object, new TypeReference<Map<String, Object>>() {});
    }

}

