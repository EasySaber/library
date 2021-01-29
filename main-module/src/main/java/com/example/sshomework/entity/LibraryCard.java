package com.example.sshomework.entity;

import lombok.*;

import javax.persistence.*;
import java.time.Period;
import java.time.ZonedDateTime;

/**
 * @author Aleksey Romodin
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@Table(name = "library_card")
public class LibraryCard extends MainEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    @EqualsAndHashCode.Exclude
    private ZonedDateTime dateTimeReturn = ZonedDateTime.now().plusDays(7);

    public Long daysDept() {
        long daysDept = Period.between(dateTimeReturn.toLocalDate(),
                ZonedDateTime.now().toLocalDate()).getDays();
        return daysDept < 0 ? 0 : daysDept;
    }
}
