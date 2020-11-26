package com.example.sshomework.entity;

import com.example.sshomework.dto.view.View;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

/**
 * @author Aleksey Romodin
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "library_card")
public class LibraryCard extends MainEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(View.Private.class)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    @JsonView(View.PersonOfAllTheBook.class)
    private Book book;


    @ManyToOne
    @JoinColumn(name = "person_id")
    @JsonView(View.Private.class)
    private Person person;

    @JsonView(View.PersonOfAllTheBook.class)
    private ZonedDateTime dateTimeReturn = ZonedDateTime.now().plusDays(7);

    @JsonView(View.Private.class)
    public Long daysDept() {
        long daysDept = ChronoUnit.DAYS.between(dateTimeReturn, ZonedDateTime.now());
        return daysDept < 0 ? 0 : daysDept;
    }
}
