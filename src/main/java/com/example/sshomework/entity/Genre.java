package com.example.sshomework.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

/**
 * @author Aleksey Romodin
 */
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
@ToString
@Table(name = "genre")
public class Genre extends MainEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_name")
    private String genreName;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "book_genre",
            joinColumns = @JoinColumn(name = "genre_id"),
            inverseJoinColumns = @JoinColumn(name = "book_id"))
    private Set<Book> books;
}
