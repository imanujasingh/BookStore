package com.project.Book.Controller;

import com.project.Book.Entities.Book;
import com.project.Book.Repository.BookRepository;
import com.project.Book.Service.BookService;
import com.project.Book.Dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.project.Book.Helper.Constants.Book_URL_Mapping;


@Controller
@RequestMapping(Book_URL_Mapping)
@ResponseBody
@Slf4j
public class BookController {

    @Autowired
    BookService bookService;

    @PostMapping("/createBook")
    public ResponseEntity<?> createBook(@RequestBody  BookDto bookdto){
        log.info("Inside CreateBook Controller");
        return bookService.createBook(bookdto);
    }

    @GetMapping("/AllBooks")
    public ResponseEntity<?> getAllBook(){
        log.info("Inside getAllBook Controller");
        return bookService.getAllBook();
    }

    @GetMapping("/AllBooksByAuthor")
    public ResponseEntity<?> getAllBookByAuthor(@RequestParam String author){
        log.info("Inside getAllBookByAuthor Controller");
        return bookService.getAllBooksByAuthor(author);
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<?> getABook(@PathVariable Integer id){
        log.info("Inside getABook Controller");
       return bookService.getABook(id);
    }

    @PutMapping("/updateBook")
    public ResponseEntity<?>updateBook(@RequestBody BookDto bookDto){
        log.info("Inside updateBook Controller");
        return bookService.updateBook(bookDto);
    }

    @DeleteMapping("/deleteBook/{id}")
    public ResponseEntity<?>deleteBook(@PathVariable Integer id){
        log.info("Inside deleteBook Controller");
        return bookService.deleteBook(id);
    }

    @PostMapping(value = "/importExcel", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("Inside importExcel Controller");
        return bookService.importExcel(file);
    }
}
