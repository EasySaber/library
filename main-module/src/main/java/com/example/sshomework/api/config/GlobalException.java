package com.example.sshomework.api.config;

import com.example.sshomework.exception.DeleteRelatedDataException;
import com.example.sshomework.exception.NotUniqueValueException;
import com.example.sshomework.exception.PersonBookDebtException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.webjars.NotFoundException;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Aleksey Romodin
 */

@RestControllerAdvice
public class GlobalException {

    /**
     * Невалидные переданные данные, при добавлении нового обьекта
     * @param ex Исключение
     * @return Ошибка
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> notValidMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

    /**
     * Невалидные переданные данные, при фильтрации записей
     * @param ex Исключение
     * @return Ошибка
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class)
    public Map<String, String> notValidBindException(BindException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((errorBe) ->
            errors.put(((FieldError) errorBe).getField(), errorBe.getDefaultMessage()));
        return errors;
    }

    /**
     * Обработка исключений "объект не найден"
     * @param ex Исключение
     * @return Ошибка
     * @throws JsonProcessingException Возможное исключение при парсинге
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public String notFoundException(NotFoundException ex) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(ex.getMessage());
    }

    /**
     * Обработка запрета на удаление связанных данных
     * @param ex Исключение
     * @return Ошибка
     * @throws JsonProcessingException Возможное исключение при парсинге
     */
    @ResponseStatus(HttpStatus.LOCKED)
    @ExceptionHandler(DeleteRelatedDataException.class)
    public String notDeleteException(DeleteRelatedDataException ex) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(ex.getMessage());
    }

    /**
     * Обработка исключения на добавление новой карточки у пользователя, при наличии задолженности
     * @param ex Исключение
     * @return Ошибка
     * @throws JsonProcessingException Возможное исключение при парсинге
     */
    @ResponseStatus(HttpStatus.LOCKED)
    @ExceptionHandler(PersonBookDebtException.class)
    public String personBookDebtException(PersonBookDebtException ex) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(ex.getMessage());
    }

    /**
     * Обработка исключения на уникальность значений в БД
     * @param ex Исключение
     * @return Ошибка
     * @throws JsonProcessingException Возможное исключение при парсинге
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NotUniqueValueException.class)
    public String notUniqueValueException(NotUniqueValueException ex) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(ex.getMessage());
    }

}
