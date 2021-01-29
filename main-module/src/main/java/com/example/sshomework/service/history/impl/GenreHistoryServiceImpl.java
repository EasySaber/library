package com.example.sshomework.service.history.impl;

import com.example.sshomework.config.Serializer.BookOfEntityHistorySerializer;
import com.example.sshomework.config.Serializer.ZonedDateTimeSerializer;
import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.entity.history.GenreHistory;
import com.example.sshomework.entity.history.OperationHistory;
import com.example.sshomework.mappers.history.GenreHistoryMapper;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.repository.history.GenreHistoryRepository;
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
@Service("genreHistoryService")
public class GenreHistoryServiceImpl extends ParentHistoryService implements HistoryService {

    private final GenreRepository repository;
    private final GenreHistoryMapper historyMappers;
    private final GenreHistoryRepository historyRepository;

    private Genre beforeEntity;
    private final ObjectMapper oMapper = new ObjectMapper();
    private final Map<String, Object> differenceMap = new HashMap<>();

    @Autowired
    public GenreHistoryServiceImpl(GenreRepository repository,
                                   GenreHistoryMapper historyMappers,
                                   GenreHistoryRepository historyRepository) {
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

        Genre afterEntity = repository.findById(entityId).orElse(null);
        OperationHistory operation = getOperation(method, afterEntity);

        //Наименование сущности (ФИО)
        String fullEntityName = (operation == OperationHistory.DELETE) ?
                beforeEntity.getGenreName() : ((afterEntity != null) ? afterEntity.getGenreName() : null);

        //Параметры сериализации
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer())
                .addSerializer(Book.class, new BookOfEntityHistorySerializer());
        oMapper.registerModule(module);

        //При обновлении записи: сравнение полей и формирование измененных данных
        if ((operation == OperationHistory.UPDATE) && (afterEntity != null)) {
            Set<Book> beforeBooks = Optional.ofNullable(beforeEntity.getBooks()).orElse(null);
            Set<Book> afterBooks = Optional.ofNullable(afterEntity.getBooks()).orElse(null);

            putMapDifference(differenceMap, beforeEntity.getGenreName(), afterEntity.getGenreName(), "genreName");
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
        GenreHistory entityHistory = new GenreHistory();
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
