package com.example.sshomework.aspect;

import com.example.sshomework.entity.Author;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import com.example.sshomework.entity.Person;
import com.example.sshomework.service.history.EntityHistory;
import com.example.sshomework.service.history.HistoryService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Aspect
@Component
public class HistoryAspect {

    private final HistoryService historyService;

    @Autowired
    public HistoryAspect(HistoryService historyService) {
        this.historyService = historyService;
    }

    @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.save(*))")
    public void whenSave() {
    }

    @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.delete(*))")
    public void whenDelete() {
    }

    @Pointcut("execution(* org.springframework.data.repository.CrudRepository+.deleteAll(*))")
    public void whenDeleteAll() {
    }

    /**
     * Добавление истории в таблицу "GenreHistory" (редактирование/добавление/удаление)
     * @param genre Жанр
     */
    @AfterReturning(value = "(whenSave() || whenDelete()) && args(genre)")
    public void addGenreHistory(JoinPoint joinPoint, Genre genre) {
        String method = joinPoint.getSignature().getName();
        historyService.addNewEntry(genre.getId(), genre.getGenreName(), EntityHistory.GENRE, method);
    }

    /**
     * Добавление истории в таблицу "AuthorHistory" (редактирование/добавление/удаление)
     * @param author Автор
     */
    @AfterReturning(value = "(whenSave() || whenDelete()) && args(author)")
    public void addAuthorHistory(JoinPoint joinPoint, Author author) {
        String method = joinPoint.getSignature().getName();
        String fullName = author.getFirstName() + " " + author.getMiddleName() + " " + author.getLastName();
        historyService.addNewEntry(author.getId(), fullName, EntityHistory.AUTHOR, method);
    }

    /**
     * Добавление истории в таблицу "BookHistory" (редактирование/добавление/удаление)
     * @param book книга
     */
    @AfterReturning(value = "(whenSave() || whenDelete()) && args(book)")
    public void addBookHistory(JoinPoint joinPoint, Book book) {
        String method = joinPoint.getSignature().getName();
        historyService.addNewEntry(book.getId(), book.getBookName(), EntityHistory.BOOK, method);
    }

    /**
     * Добавление истории в таблицу "PersonHistory" (редактирование/добавление/удаление)
     * @param person Пользователь
     */
    @AfterReturning(value = "(whenSave() || whenDelete()) && args(person)")
    public void addPersonHistory(JoinPoint joinPoint, Person person) {
        String method = joinPoint.getSignature().getName();
        String fullName = person.getFirstName() + " " + person.getMiddleName() + " " + person.getLastName();
        historyService.addNewEntry(person.getId(), fullName, EntityHistory.PERSON, method);
    }

    /**
     * Добавление истории в таблицу "PersonHistory" (редактирование/добавление/удаление)
     * Удаление списком
     * @param persons Пользователь
     */
    @AfterReturning(value = "whenDeleteAll() && args(persons)")
    public void addPersonHistoryDeleteAll(JoinPoint joinPoint, List<Person> persons) {
        String method = joinPoint.getSignature().getName();
        persons.forEach(person -> {
            String fullName = person.getFirstName() + " " + person.getMiddleName() + " " + person.getLastName();
            historyService.addNewEntry(person.getId(), fullName, EntityHistory.PERSON, method);
        });

    }


}
