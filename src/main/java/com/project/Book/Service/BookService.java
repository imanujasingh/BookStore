package com.project.Book.Service;

import com.project.Book.Entities.Book;
import com.project.Book.Repository.BookRepository;
import com.project.Book.dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BookService {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    ModelMapper modelMapper;

    public ResponseEntity<?> createBook(@NotNull BookDto bookDto) {
        log.info("Inside createBook Service");
        Book book = new Book();
        Optional<Book> existingBook = bookRepository.findById(bookDto.getId());

        if (existingBook.isPresent()) {
            // If a book with the same ID already exists, return an error response
            log.error("A book with ID " + bookDto.getId() + " already exists.");
            return ResponseEntity.badRequest().body("A book with ID " + bookDto.getId() + " already exists.");
        }
        book.setId((int) bookDto.getId());
        book.setIsbn(bookDto.getIsbn());
        book.setPublication(bookDto.getPublication());
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());

        // Save the book entity to the repository.
        book = bookRepository.save(book);

        // Convert the saved book entity back to a DTO
        BookDto savedBookDto = modelMapper.map(book,BookDto.class);

        // Return the created book DTO
        return ResponseEntity.ok(savedBookDto);
    }


    public ResponseEntity<?> getABook(@PathVariable Integer id) {
        log.info("Inside getABook Service");
        Optional<Book> getBook = bookRepository.findById(id);
        if (getBook.isPresent()) {
            return ResponseEntity.ok().body(getBook);
        } else {
            // If a book with the same ID already exists, return an error response
            log.error("This id " + id + " doesn't exist in the Inventory");
            return ResponseEntity.badRequest().body("This id " + id + " doesn't exist in the Inventory");
        }
    }

    public ResponseEntity<List<Book>> getAllBook() {
        log.info("Inside AllBook Service");
        List<Book> bookList = new ArrayList<>();
        bookList = bookRepository.findAll();
        if (bookList.isEmpty()) {
            // If Nothing is there
            log.error("EMPTY INVENTORY LIST");
            return ResponseEntity.badRequest().body(bookList);
        }
        return ResponseEntity.ok().body(bookList);
    }

    public ResponseEntity<?> updateBook(@NotNull BookDto bookDto) {
        log.info("Inside updateBook Service");
        Optional<Book> bookIdToBeUpdated = bookRepository.findById(bookDto.getId());
        if (bookIdToBeUpdated.isPresent()) {
            Book book = bookIdToBeUpdated.get();
            bookRepository.updateBook(book.getId(), bookDto);

            BookDto savedBookDto = modelMapper.map(book,BookDto.class);
            return ResponseEntity.ok().body(savedBookDto);
        } else {
            return ResponseEntity.badRequest().body("Book with Id " + bookDto.getId() + " is not present in Inventory");
        }
    }

    public ResponseEntity<?> deleteBook(Integer id) {
        log.info("Inside DeleteBook Service");
        Optional<Book> book = bookRepository.findById(id);
        if (book.isPresent()) {
            log.info("Deleted Book with id " + id);
            bookRepository.deleteById(id);
            return ResponseEntity.ok().body("Book with id " + id + " Deleted ");
        } else {
            log.info("Book with id " + id + " not present in Inventory");
            return ResponseEntity.badRequest().body("Book with id " + id + " not present in Inventory");
        }
    }
}
