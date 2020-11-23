package com.example.sshomework.entity;

import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "author")
public class Author {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.Public.class, View.Private.class, View.PersonOfAllTheBookSmall.class,
            View.Book.class})
    private Long id;

    @JsonView({View.Public.class, View.Private.class, View.PersonOfAllTheBookSmall.class, View.BookPost.class})
    private String firstName;

    @JsonView({View.Public.class, View.Private.class, View.PersonOfAllTheBookSmall.class, View.BookPost.class})
    private String middleName;

    @JsonView({View.Public.class, View.Private.class, View.PersonOfAllTheBookSmall.class, View.BookPost.class})
    private String lastName;

    @JsonView(View.Public.class)
    @OneToMany(mappedBy = "authorBook", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Set<Book> books;
}
