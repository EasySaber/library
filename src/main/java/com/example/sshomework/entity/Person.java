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
    @Column(name = "first_name")
    private String firstName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    @Column(name = "middle_name")
    private String middleName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    @Column(name = "last_name")
    private String lastName;

    @JsonView({View.Public.class, View.PersonOfAllTheBookSmall.class})
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @JsonView(View.PersonOfAllTheBookSmall.class)
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "library_card",
            joinColumns = @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books;
}
