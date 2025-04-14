package com.project.Book.Entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "book")
public class Book {

    @Id
    private long id;
    private String title;
    private String author;
    private long isbn;
    private String publication;

    public Book(){
        super();
    }


}
