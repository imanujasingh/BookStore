package com.project.Book.Repository;

import com.project.Book.Entities.Book;
import com.project.Book.dto.BookDto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book,Integer> {
    //JPQL
    @Query("select b from Book b WHERE b.author = :author")
    List<Book> findByAuthor(@Param("author") String author);

    @Transactional
    @Modifying
    @Query("UPDATE Book b " +
            "SET b.title = CASE WHEN :#{#bookDto.title} IS NULL THEN b.title ELSE :#{#bookDto.title} END, " +
            "    b.author = CASE WHEN :#{#bookDto.author} IS NULL THEN b.author ELSE :#{#bookDto.author} END, " +
            "    b.publication = CASE WHEN :#{#bookDto.publication} IS NULL THEN b.publication ELSE :#{#bookDto.publication} END, " +
            "    b.isbn = CASE WHEN :#{#bookDto.isbn} IS NULL THEN b.isbn ELSE :#{#bookDto.isbn} END " +
            "WHERE b.id = :bookId")
    void updateBook(@Param("bookId") Long bookId, @Param("bookDto") BookDto bookDto);

}
