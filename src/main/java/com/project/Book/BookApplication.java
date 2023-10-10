package com.project.Book;

import com.project.Book.Repository.BookRepository;
import com.project.Book.Entities.Book;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;


@SpringBootApplication
//@EnableSwagger2
public class BookApplication {

	public static void main(String[] args) {

		ApplicationContext context = SpringApplication.run(BookApplication.class, args);
	}

}
