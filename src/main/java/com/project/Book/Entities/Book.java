package com.project.Book.Entities;

import jakarta.persistence.*;
import lombok.Getter;


@Entity
@Getter
@Table(name = "book")
public class Book {

    @Id
    private long id;
    private String title;
    private String author;
    private long isbn;
    private String publication;
    public Book(int id, String title, String author, long isbn, String publication) {
        super();
        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publication = publication;
    }

    public Book(){
        super();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIsbn(long isbn) {
        this.isbn = isbn;
    }

    public void setPublication(String publication) {
        this.publication = publication;
    }

    public void setId(int id) {
        this.id = id;
    }
}
