package com.example.sshomework.service.history.impl;

import com.example.sshomework.config.Serializer.BookOfEntityHistorySerializer;
import com.example.sshomework.config.Serializer.ZonedDateTimeSerializer;
import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.history.AuthorHistory;
import com.example.sshomework.entity.history.OperationHistory;
import com.example.sshomework.mappers.history.AuthorHistoryMapper;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.repository.history.AuthorHistoryRepository;
import com.example.sshomework.service.history.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Aleksey Romodin
 */
@Service("authorHistoryService")
public class AuthorHistoryServiceImpl extends ParentHistoryService implements HistoryService {

    private final AuthorRepository repository;
    private final AuthorHistoryMapper historyMappers;
    private final AuthorHistoryRepository historyRepository;

    private Author beforeEntity;
    private final ObjectMapper oMapper = new ObjectMapper();
    private final Map<String, Object> differenceMap = new HashMap<>();

    @Autowired
    public AuthorHistoryServiceImpl(AuthorRepository repository,
                                    AuthorHistoryMapper historyMappers,
                                    AuthorHistoryRepository historyRepository) {
        this.repository = repository;
        this.historyMappers = historyMappers;
        this.historyRepository = historyRepository;
    }

    /**
     * Сохраниее истории действий над сущностью
     * @param entityId Сохраненная сущность(id) в БД
     * @param method Характеристика операции над сущностью save/delete
     */
    @Override
    public void addNewHistory(Long entityId, String method) {
        differenceMap.clear();

        Author afterEntity = repository.findById(entityId).orElse(null);
        OperationHistory operation = getOperation(method, afterEntity);

        //Наименование сущности (ФИО)
        String fullEntityName = (operation == OperationHistory.DELETE) ?
                (beforeEntity.getFirstName() + " " + beforeEntity.getMiddleName() + " " + beforeEntity.getLastName()) :
                ((afterEntity != null) ? (afterEntity.getFirstName() + " " + afterEntity.getMiddleName() + " " + afterEntity.getLastName()) : null);

        //Параметры сериализации
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer())
                .addSerializer(Book.class, new BookOfEntityHistorySerializer());
        oMapper.registerModule(module);

        //При обновлении записи: сравнение полей и формирование измененных данных
        if ((operation == OperationHistory.UPDATE) && (afterEntity != null)) {
            Set<Book> beforeBooks = Optional.ofNullable(beforeEntity.getBooks()).orElse(null);
            Set<Book> afterBooks = Optional.ofNullable(afterEntity.getBooks()).orElse(null);

            putMapDifference(differenceMap, beforeEntity.getFirstName(), afterEntity.getFirstName(), "firstName");
            putMapDifference(differenceMap, beforeEntity.getMiddleName(), afterEntity.getMiddleName(), "middleName");
            putMapDifference(differenceMap, beforeEntity.getLastName(), afterEntity.getLastName(), "lastName");
            putMapDifference(differenceMap, beforeEntity.getDateTimeOfBirth(), afterEntity.getDateTimeOfBirth(), "dateTimeOfBirth");
            putMapDifference(differenceMap,
                    ((beforeBooks != null) ? getSetMapFromSetObject(oMapper, beforeBooks) : null),
                    ((afterBooks!= null) ? getSetMapFromSetObject(oMapper, afterBooks) : null),
                    "books");
            if (!differenceMap.isEmpty()) {
                differenceMap.put("id", entityId);
            }
        }

        //Сущность до совершения опереции
        Map<String, Object> beforeMap = getMapFromEntity(oMapper, beforeEntity, operation, "before");
        //Сущность после совершения опереции
        Map<String, Object> afterMap = getMapFromEntity(oMapper, afterEntity, operation, "after");

        //Запись в историю
        AuthorHistory entityHistory = new AuthorHistory();
        entityHistory.setEntityId(entityId);
        entityHistory.setEntityName(fullEntityName);
        entityHistory.setOperation(operation);
        entityHistory.setUserName(getUsername());
        entityHistory.setBefore(beforeMap);
        entityHistory.setAfter(afterMap);
        entityHistory.setDifference(differenceMap.isEmpty() ? null : differenceMap);
        historyRepository.save(entityHistory);
    }

    /**
     * Получение истории опереций над сущностью
     * @param entityId id сущности
     * @return Список опереций
     */
    @Override
    public List<HistoryDto> getHistoryEntity(Long entityId) {
        return historyMappers.toDtoList(historyRepository.findByEntityId(entityId));
    }

    /**
     * Получение сущности из БД перед совершением операции для сравнения
     * @param entityId Сущность(id сущности)
     */
    @Override
    public void commitEntity(Long entityId) {
        beforeEntity = (entityId != null) ? repository.findById(entityId).orElse(null) : null;
    }
}
