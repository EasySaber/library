package com.example.sshomework.service.history;

import com.example.sshomework.dto.history.HistoryDto;
import com.example.sshomework.entity.MainEntity;
import com.example.sshomework.entity.history.AuthorHistory;
import com.example.sshomework.entity.history.BookHistory;
import com.example.sshomework.entity.history.GenreHistory;
import com.example.sshomework.entity.history.PersonHistory;
import com.example.sshomework.mappers.history.AuthorHistoryMapper;
import com.example.sshomework.mappers.history.BookHistoryMapper;
import com.example.sshomework.mappers.history.GenreHistoryMapper;
import com.example.sshomework.mappers.history.PersonHistoryMapper;
import com.example.sshomework.repository.GenreRepository;
import com.example.sshomework.repository.PersonRepository;
import com.example.sshomework.repository.author.AuthorRepository;
import com.example.sshomework.repository.book.BookRepository;
import com.example.sshomework.repository.history.AuthorHistoryRepository;
import com.example.sshomework.repository.history.BookHistoryRepository;
import com.example.sshomework.repository.history.GenreHistoryRepository;
import com.example.sshomework.repository.history.PersonHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.webjars.NotFoundException;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
public class HistoryServiceImpl implements HistoryService{

    private final GenreHistoryRepository genreHistoryRepository;
    private final AuthorHistoryRepository authorHistoryRepository;
    private final PersonHistoryRepository personHistoryRepository;
    private final BookHistoryRepository bookHistoryRepository;
    private final GenreRepository genreRepository;
    private final AuthorRepository authorRepository;
    private final PersonRepository personRepository;
    private final BookRepository bookRepository;
    private final GenreHistoryMapper genreMapper;
    private final PersonHistoryMapper personMapper;
    private final BookHistoryMapper bookMapper;
    private final AuthorHistoryMapper authorMapper;


    @Autowired
    public HistoryServiceImpl(GenreHistoryRepository genreHistoryRepository,
                              AuthorHistoryRepository authorHistoryRepository,
                              PersonHistoryRepository personHistoryRepository,
                              BookHistoryRepository bookHistoryRepository,
                              GenreRepository genreRepository,
                              AuthorRepository authorRepository,
                              PersonRepository personRepository,
                              BookRepository bookRepository,
                              GenreHistoryMapper genreMapper,
                              PersonHistoryMapper personMapper,
                              BookHistoryMapper bookMapper,
                              AuthorHistoryMapper authorMapper)
    {
        this.genreHistoryRepository = genreHistoryRepository;
        this.authorHistoryRepository = authorHistoryRepository;
        this.personHistoryRepository = personHistoryRepository;
        this.bookHistoryRepository = bookHistoryRepository;
        this.genreRepository = genreRepository;
        this.authorRepository = authorRepository;
        this.personRepository = personRepository;
        this.bookRepository = bookRepository;
        this.genreMapper = genreMapper;
        this.personMapper = personMapper;
        this.bookMapper = bookMapper;
        this.authorMapper = authorMapper;
    }

    @Override
    public void addNewEntry(Long entityId, String entityName, EntityHistory entityHistory, String method) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OperationHistory op;

        switch (entityHistory) {
            case BOOK: {
                op = method.equals("save") ?
                        getOperationHistory(bookRepository.findById(entityId).map(MainEntity::getVersion).orElse(0L)) :
                        OperationHistory.DELETE;
                BookHistory bookHistory = new BookHistory();
                bookHistory.setEntityId(entityId);
                bookHistory.setEntityName(entityName);
                bookHistory.setOperation(op.toString());
                bookHistory.setUserName(auth.getName());
                bookHistoryRepository.save(bookHistory);
                break;
            }
            case GENRE: {
                op = method.equals("save") ?
                        getOperationHistory(genreRepository.findById(entityId).map(MainEntity::getVersion).orElse(0L)) :
                        OperationHistory.DELETE;
                GenreHistory genreHistory = new GenreHistory();
                genreHistory.setEntityId(entityId);
                genreHistory.setEntityName(entityName);
                genreHistory.setOperation(op.toString());
                genreHistory.setUserName(auth.getName());
                genreHistoryRepository.save(genreHistory);
                break;
            }
            case AUTHOR: {
                op = method.equals("save") ?
                        getOperationHistory(authorRepository.findById(entityId).map(MainEntity::getVersion).orElse(0L)) :
                        OperationHistory.DELETE;
                AuthorHistory authorHistory = new AuthorHistory();
                authorHistory.setEntityId(entityId);
                authorHistory.setEntityName(entityName);
                authorHistory.setOperation(op.toString());
                authorHistory.setUserName(auth.getName());
                authorHistoryRepository.save(authorHistory);
                break;
            }
            case PERSON: {
                op = method.equals("save") ?
                        getOperationHistory(personRepository.findById(entityId).map(MainEntity::getVersion).orElse(0L)) :
                        OperationHistory.DELETE;
                PersonHistory personHistory = new PersonHistory();
                personHistory.setEntityId(entityId);
                personHistory.setEntityName(entityName);
                personHistory.setOperation(op.toString());
                personHistory.setUserName(auth.getName());
                personHistoryRepository.save(personHistory);
                break;
            }
        }
    }

    @Override
    public List<HistoryDto> getHistory(Long entityId, EntityHistory entityHistory) {
        switch (entityHistory) {
            case AUTHOR:
                return authorMapper.toDtoList(authorHistoryRepository.findByEntityId(entityId));
            case GENRE:
                return genreMapper.toDtoList(genreHistoryRepository.findByEntityId(entityId));
            case BOOK:
                return bookMapper.toDtoList(bookHistoryRepository.findByEntityId(entityId));
            case PERSON:
                return personMapper.toDtoList(personHistoryRepository.findByEntityId(entityId));
            default: throw new NotFoundException("Сущность не найдена");
        }
    }

    private OperationHistory getOperationHistory(Long version) {
        OperationHistory op;
        op = (version == 0) ? OperationHistory.CREATE : OperationHistory.UPDATE;
        return op;
    }
}
