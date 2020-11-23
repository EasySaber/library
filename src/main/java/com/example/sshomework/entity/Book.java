package com.example.sshomework.entity;

import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.*;

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
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.All.class, View.PersonOfAllTheBook.class, View.UpdateBookGenres.class})
    private Long id;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class, View.Book.class})
    @Column(name = "book_name")
    private String bookName;


    @ManyToOne
    @JoinColumn(name = "author_id")
    @JsonView({View.All.class, View.PersonOfAllTheBook.class, View.Book.class})
    private Author authorBook;

    @JsonView({View.Public.class, View.PersonOfAllTheBook.class, View.Book.class, View.UpdateBookGenres.class})
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @JsonView(View.All.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "library_card",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Person> persons;
}
