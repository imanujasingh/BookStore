package com.project.Book.Dto;

import com.project.Book.Entities.Book;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class BookResponse {
    List<Book> books;
    int count;
    public BookResponse(List<Book> bookList){
        this.books=bookList;
        this.count=bookList.size();
    }
}
// This class is used to encapsulate the response of a book-related operation.