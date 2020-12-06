package com.example.sshomework.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
    private Long id;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    @ManyToOne
    @JoinColumn(name = "person_id")
    private Person person;

    private ZonedDateTime dateTimeReturn = ZonedDateTime.now().plusDays(7);

    public Long daysDept() {
        long daysDept = ChronoUnit.DAYS.between(dateTimeReturn, ZonedDateTime.now());
        return daysDept < 0 ? 0 : daysDept;
    }
}
