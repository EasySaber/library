package com.example.sshomework.entity;

import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
@Table(name = "genre")
public class Genre extends MainEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.All.class, View.PersonOfAllTheBook.class, View.Book.class, View.UpdateBookGenres.class})
    private Long id;

    @JsonView({View.Public.class, View.PersonOfAllTheBook.class, View.BookPost.class})
    @Column(name = "genre_name")
    private String genreName;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books;
}
