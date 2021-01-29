package com.example.sshomework.service.history.impl;

import com.example.sshomework.config.Serializer.LocalDateSerializer;
import com.example.sshomework.config.Serializer.PersonLibraryCardHistorySerializer;
import com.example.sshomework.config.Serializer.ZonedDateTimeSerializer;
import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.entity.LibraryCard;
import com.example.sshomework.entity.Person;
import com.example.sshomework.entity.User;
import com.example.sshomework.entity.history.OperationHistory;
import com.example.sshomework.entity.history.PersonHistory;
import com.example.sshomework.mappers.history.PersonHistoryMapper;
import com.example.sshomework.repository.PersonRepository;
import com.example.sshomework.repository.history.PersonHistoryRepository;
import com.example.sshomework.service.history.HistoryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.*;

/**
 * @author Aleksey Romodin
 */
@Service("personHistoryService")
public class PersonHistoryServiceImpl extends ParentHistoryService implements HistoryService {

    private final PersonRepository repository;
    private final PersonHistoryMapper historyMappers;
    private final PersonHistoryRepository historyRepository;

    private Person beforeEntity;
    private final ObjectMapper oMapper = new ObjectMapper();
    private final Map<String, Object> differenceMap = new HashMap<>();

    @Autowired
    public PersonHistoryServiceImpl(PersonHistoryRepository personHistoryRepository,
                                    PersonRepository personRepository,
                                    PersonHistoryMapper personHistoryMapper) {
        this.historyRepository = personHistoryRepository;
        this.repository = personRepository;
        this.historyMappers = personHistoryMapper;
    }

    /**
     * Сохраниее истории действий над сущностью
     * @param entityId Сохраненная сущность(id) в БД
     * @param method Характеристика операции над сущностью save/delete
     */    
    @Override
    public void addNewHistory(Long entityId, String method) {
        differenceMap.clear();

        Person afterEntity = repository.findById(entityId).orElse(null);
        OperationHistory operation = getOperation(method, afterEntity);

        //Наименование сущности (ФИО)
        String fullEntityName = (operation == OperationHistory.DELETE) ?
                (beforeEntity.getFirstName() + " " + beforeEntity.getMiddleName() + " " + beforeEntity.getLastName()) :
                ((afterEntity != null) ? (afterEntity.getFirstName() + " " + afterEntity.getMiddleName() + " " + afterEntity.getLastName()) : null);

        //Параметры сериализации
        SimpleModule module = new SimpleModule();
        module.addSerializer(ZonedDateTime.class, new ZonedDateTimeSerializer())
                .addSerializer(LocalDate.class, new LocalDateSerializer())
                .addSerializer(LibraryCard.class, new PersonLibraryCardHistorySerializer());
        oMapper.registerModule(module);

        //При обновлении записи: сравнение полей и формирование измененных данных
        if ((operation == OperationHistory.UPDATE) && (afterEntity != null)) {
            Set<LibraryCard> beforeBooks = Optional.ofNullable(beforeEntity.getBooks()).orElse(null);
            Set<LibraryCard> afterBooks = Optional.ofNullable(afterEntity.getBooks()).orElse(null);
            String beforeAccount = Optional.ofNullable(beforeEntity.getAccount()).map(User::getUsername).orElse(null);
            String afterAccount =  Optional.ofNullable(afterEntity.getAccount()).map(User::getUsername).orElse(null);

            putMapDifference(differenceMap, beforeEntity.getFirstName(), afterEntity.getFirstName(), "firstName");
            putMapDifference(differenceMap, beforeEntity.getMiddleName(), afterEntity.getMiddleName(), "middleName");
            putMapDifference(differenceMap, beforeEntity.getLastName(), afterEntity.getLastName(), "lastName");
            putMapDifference(differenceMap, beforeEntity.getDateOfBirth(),afterEntity.getDateOfBirth(), "dateOfBirth");
            putMapDifference(differenceMap, beforeAccount, afterAccount,"account");
            putMapDifference(differenceMap,
                    ((beforeBooks != null) ? getSetMapFromSetObject(oMapper, beforeBooks) : null),
                    ((afterBooks!= null) ? getSetMapFromSetObject(oMapper, afterBooks) : null),
                    "cards");
            if (!differenceMap.isEmpty()) {
                differenceMap.put("id", entityId);
            }
        }

        //Сущность до совершения опереции
        Map<String, Object> beforeMap = getMapFromEntity(oMapper, beforeEntity, operation, "before");
        //Сущность после совершения опереции
        Map<String, Object> afterMap = getMapFromEntity(oMapper, afterEntity, operation, "after");

        //Запись в историю
        PersonHistory entityHistory = new PersonHistory();
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
     * Получение истории операций над сущностью
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
