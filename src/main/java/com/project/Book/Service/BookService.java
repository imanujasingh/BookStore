package com.project.Book.Service;

import com.project.Book.Dto.BookResponse;
import com.project.Book.Entities.Book;
import com.project.Book.Repository.BookRepository;
import com.project.Book.Dto.BookDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.IOException;
import java.io.InputStream;
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
        book.setId(bookDto.getId());
        book.setIsbn(bookDto.getIsbn());
        book.setPublication(bookDto.getPublication());
        book.setAuthor(bookDto.getAuthor());
        book.setTitle(bookDto.getTitle());

        // Save the book entity to the repository.
        book = bookRepository.save(book);

        // Convert the saved book entity back to a DTO
        BookDto savedBookDto = modelMapper.map(book, BookDto.class);

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

    public ResponseEntity<?> getAllBook() {
        log.info("Inside AllBook Service");
        List<Book> bookList = new ArrayList<>();
        bookList = bookRepository.findAllByOrderByIdAsc();
        if (bookList.isEmpty()) {
            // If Nothing is there
            log.error("EMPTY INVENTORY LIST");
            return ResponseEntity.badRequest().body(new BookResponse(bookList));
        }
        BookResponse bookResponse = new BookResponse(bookList);
        return ResponseEntity.ok().body(bookResponse);
    }

    public ResponseEntity<?> updateBook(@NotNull BookDto bookDto) {
        log.info("Inside updateBook Service");
        Optional<Book> bookIdToBeUpdated = bookRepository.findById(bookDto.getId());
        if (bookIdToBeUpdated.isPresent()) {
            Book book = bookIdToBeUpdated.get();
            bookRepository.updateBook(book.getId(), bookDto);

            BookDto savedBookDto = modelMapper.map(book, BookDto.class);
            return ResponseEntity.ok().body("Book Updated Successfully: " + savedBookDto);
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
    public ResponseEntity<?> importExcel(MultipartFile file) throws IOException {
        log.info("Inside importExcel Service");
        List<Book> bookList = new ArrayList<>();
        int successfulEntries = 0;

//        if (!file.getOriginalFilename().endsWith(".xlsx")) {
//            log.error("Invalid file type. Only .xlsx files are allowed.");
//            return ResponseEntity.badRequest().body("Invalid file type. Only .xlsx files are allowed.");
//        }
        try (InputStream inputStream = file.getInputStream()) {
            Workbook workbook = new XSSFWorkbook(inputStream); // Pass InputStream here
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) { // Skip the header row
                    continue;
                }
                try {
                    Book book = new Book();

                    book.setId((long) row.getCell(0).getNumericCellValue()); // Adjusted to long
                    book.setTitle(row.getCell(1).getStringCellValue());
                    book.setAuthor(row.getCell(2).getStringCellValue());
                    book.setPublication(row.getCell(3).getStringCellValue());
                    book.setIsbn((long) row.getCell(4).getNumericCellValue());

                    if(bookRepository.existsById((int) book.getId())) {
                        log.error("Book with ID " + book.getId() + " already exists. Skipping this entry.");
                        continue;
                    }
                    log.info("Book Imported Successfully: " + book.getId());
                    bookList.add(book);
                    successfulEntries++;
                } catch (Exception e) {
                    log.error("Error processing row " + row.getRowNum() + ": " + e.getMessage());
                }
            }

            bookRepository.saveAll(bookList);
            log.info("Total no of entries imported successfully: " + successfulEntries);
            return ResponseEntity.ok().body("Excel Imported Successfully. Total entries: " + successfulEntries);

        } catch (Exception e) {
            log.error("Error in Importing Excel: " + e.getMessage(), e);
            return ResponseEntity.badRequest().body("Error in Importing Excel: " + e.getMessage());
        }
    }

    public ResponseEntity<?> getAllBooksByAuthor(String author) {
        List<Book> bookList = new ArrayList<>();
        bookList = bookRepository.findByAuthorOrderByTitleAsc(author);
        if (bookList.isEmpty()) {
            log.error("No books found by author " + author);
            return ResponseEntity.badRequest().body("No books found by author " + author);
        }
        BookResponse bookResponse = new BookResponse(bookList);
        return ResponseEntity.ok().body(bookResponse);
    }
}


