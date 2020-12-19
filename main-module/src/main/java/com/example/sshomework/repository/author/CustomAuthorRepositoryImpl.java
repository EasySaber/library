package com.example.sshomework.repository.author;

import com.example.sshomework.dto.author.AuthorSearchRequest;
import com.example.sshomework.entity.Author;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Service
public class CustomAuthorRepositoryImpl implements CustomAuthorRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Author> customFilter(AuthorSearchRequest request) {

        List<Predicate> predicates = new ArrayList<>();

        CriteriaBuilder cBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> cQuery = cBuilder.createQuery(Author.class);
        Root<Author> author = cQuery.from(Author.class);

        String firstName = request.getFirstName();
        String middleName = request.getMiddleName();
        String lastName = request.getLastName();
        LocalDate starDateCreated = request.getStarDateCreated();
        LocalDate endDateCreated = request.getEndDateCreated();

        if (firstName != null) {
            predicates.add(cBuilder.equal(
                    cBuilder.lower(author.get("firstName")), firstName.toLowerCase()));
        }
        if (middleName != null) {
            predicates.add(cBuilder.equal(
                    cBuilder.lower(author.get("middleName")), middleName.toLowerCase()));
        }
        if (lastName != null) {
            predicates.add(cBuilder.equal(
                    cBuilder.lower(author.get("lastName")), lastName.toLowerCase()));
        }
        if ((starDateCreated != null) && (endDateCreated !=null)) {
            predicates.add(cBuilder.between(
                    cBuilder.function("date", LocalDate.class, author.get("dateTimeCreated")),
                    starDateCreated, endDateCreated));
        } else {
            if (starDateCreated != null) {
                predicates.add(cBuilder.greaterThanOrEqualTo(
                        cBuilder.function("date", LocalDate.class, author.get("dateTimeCreated")),
                        starDateCreated));
            }
            if (endDateCreated != null) {
                predicates.add(cBuilder.lessThanOrEqualTo(
                        cBuilder.function("date", LocalDate.class, author.get("dateTimeCreated")),
                        endDateCreated));
            }
        }
        if (!predicates.isEmpty()) {
            cQuery.where(predicates.toArray(new Predicate[0]));
        }

        return entityManager.createQuery(cQuery).getResultList();
    }

}
