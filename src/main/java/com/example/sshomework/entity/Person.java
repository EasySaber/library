package com.example.sshomework.entity;

import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView({View.All.class, View.PersonOfAllTheBookSmall.class})
    private Long id;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    private String firstName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    private String middleName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    private String lastName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    private LocalDate dateOfBirth;

    @JsonView(View.PersonOfAllTheBookSmall.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "library_card",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books;
}
