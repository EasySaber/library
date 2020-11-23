package com.example.sshomework.repository;

import com.example.sshomework.dto.genre.GenreStatisticsProjection;
import com.example.sshomework.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aleksey Romodin
 */
@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
    @Query(value = "SELECT a.genre_name AS genreName, count(b.book_id) AS count " +
                    "FROM genre a " +
                    "INNER JOIN book_genre b On a.id = b.genre_id " +
                    "GROUP BY a.genre_name", nativeQuery = true)
    List<GenreStatisticsProjection> getGenreStatistics();
}
