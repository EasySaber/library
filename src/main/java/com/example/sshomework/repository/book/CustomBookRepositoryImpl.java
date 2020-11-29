package com.example.sshomework.repository.book;

import com.example.sshomework.dto.book.BookSearchRequest;
import com.example.sshomework.dto.book.Sings;
import com.example.sshomework.entity.Book;
import com.example.sshomework.entity.Genre;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Aleksey Romodin
 */
@RequiredArgsConstructor
public class CustomBookRepositoryImpl implements CustomBookRepository {

    private final EntityManager entityManager;

    @Override
    public List<Book> customFilter(BookSearchRequest request) {

        List<Predicate> predicates = new ArrayList<>();

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cQuery = cBuilder.createQuery(Book.class);
        Root<Book> book = cQuery.from(Book.class);

        String genre = request.getGenre();
        Integer yearPublication = request.getYearPublication();
        Sings sing = request.getSing();

        if (genre != null) {
            Join<Book, Genre> bookGenreJoin = book.join("genres", JoinType.INNER);
            predicates.add(cBuilder.equal(bookGenreJoin.get("genreName"), genre.toLowerCase()));
        }
        if (yearPublication !=null){
            if (sing == Sings.MORE) {
                predicates.add(cBuilder.greaterThan(
                        cBuilder.function("year", Integer.class, book.get("datePublication")),
                        yearPublication));
            }
            if (sing == Sings.LESS) {
                predicates.add(cBuilder.lessThan(
                        cBuilder.function("year", Integer.class, book.get("datePublication")),
                        yearPublication));
            }
            if (sing == Sings.EQUALLY) {
                predicates.add(cBuilder.equal(
                        cBuilder.function("year", Integer.class, book.get("datePublication")),
                        yearPublication));
            }
        }

        if (!predicates.isEmpty()) {
            cQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(cQuery).getResultList();
    }
}
