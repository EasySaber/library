package com.example.sshomework.service.history.impl;

import com.example.sshomework.config.Serializer.*;
import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.entity.LibraryCard;
import com.example.sshomework.entity.history.BookHistory;
import com.example.sshomework.entity.history.OperationHistory;
import com.example.sshomework.mappers.history.BookHistoryMapper;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.repository.history.BookHistoryRepository;
import com.example.sshomework.service.history.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Aleksey Romodin
 */
@Service("bookHistoryService")
public class BookHistoryServiceImpl extends ParentHistoryService implements HistoryService {

    private final BookRepository repository;
    private final BookHistoryMapper historyMappers;
    private final BookHistoryRepository historyRepository;

    private Book beforeEntity;
    private final ObjectMapper oMapper = new ObjectMapper();
    private final Map<String, Object> differenceMap = new HashMap<>();

    public BookHistoryServiceImpl(BookRepository repository,
                                  BookHistoryMapper historyMappers,
                                  BookHistoryRepository historyRepository) {
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

        Book afterEntity = repository.findById(entityId).orElse(null);
        OperationHistory operation = getOperation(method, afterEntity);

        //Наименование сущности (ФИО)
        String fullEntityName = (operation == OperationHistory.DELETE) ?
                beforeEntity.getBookName() : ((afterEntity != null) ? (afterEntity.getBookName()) : null);

        //Параметры сериализации
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer())
                .addSerializer(LocalDate.class, new LocalDateSerializer())
                .addSerializer(LibraryCard.class, new BookLibraryCardHistorySerializer())
                .addSerializer(Author.class, new BookAuthorSerializer())
                .addSerializer(Genre.class, new BookGenreSerializer());
        oMapper.registerModule(module);

        //При обновлении записи: сравнение полей и формирование измененных данных
        if ((operation == OperationHistory.UPDATE) && (afterEntity != null)) {
            Set<LibraryCard> beforePersons = Optional.ofNullable(beforeEntity.getPersons()).orElse(null);
            Set<LibraryCard> afterPersons = Optional.ofNullable(afterEntity.getPersons()).orElse(null);

            Set<Genre> afterGenres = Optional.ofNullable(afterEntity.getGenres()).orElse(null);
            Set<Genre> beforeGenres = Optional.ofNullable(beforeEntity.getGenres()).orElse(null);

            putMapDifference(differenceMap, beforeEntity.getBookName(), afterEntity.getBookName(), "bookName");
            putMapDifference(differenceMap, beforeEntity.getDatePublication(), afterEntity.getDatePublication(), "datePublication");
            putMapDifference(differenceMap, getMapFromObject(oMapper, beforeEntity.getAuthorBook()),
                    getMapFromObject(oMapper, afterEntity.getAuthorBook()), "author");
            putMapDifference(differenceMap,
                    ((beforeGenres != null) ? getSetMapFromSetObject(oMapper, beforeGenres) : null),
                    ((afterGenres!= null) ? getSetMapFromSetObject(oMapper, afterGenres) : null), "genres");
            putMapDifference(differenceMap,
                    ((beforePersons != null) ? getSetMapFromSetObject(oMapper, beforePersons) : null),
                    ((afterPersons!= null) ? getSetMapFromSetObject(oMapper, afterPersons) : null), "cards");
            if (!differenceMap.isEmpty()) {
                differenceMap.put("id", entityId);
            }
        }

        //Сущность до совершения опереции
        Map<String, Object> beforeMap = getMapFromEntity(oMapper, beforeEntity, operation, "before");
        //Сущность после совершения опереции
        Map<String, Object> afterMap = getMapFromEntity(oMapper, afterEntity, operation, "after");

        //Запись в историю
        BookHistory entityHistory = new BookHistory();
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
