package com.example.sshomework.specification;

import com.example.sshomework.entity.Book;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 * @author Aleksey Romodin
 */
@AllArgsConstructor
public class BookSpecification implements Specification<Book> {
    private final String key;
    private final String value;

    @Override
    public Predicate toPredicate(@NonNull Root<Book> root,
                                 @NonNull CriteriaQuery<?> criteriaQuery,
                                 @NonNull CriteriaBuilder criteriaBuilder) {
        switch (key) {
            case "firstName":
                return criteriaBuilder.equal(root.get("authorBook").get("firstName"), value);
            case "middleName":
                return criteriaBuilder.equal(root.get("authorBook").get("middleName"), value);
            case "lastName":
                return criteriaBuilder.equal(root.get("authorBook").get("lastName"), value);
            default: throw new RuntimeException("unreachable");
        }
    }
}
