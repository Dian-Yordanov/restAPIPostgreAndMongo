package com.example.springboot;

import java.util.ArrayList;

public class Book {
    String id;
    String book;
    String author;

    public Book(String book, String author) {

        this.id = id;
        this.book = book;
        this.author = author;

    }

    public String getBookId(){
        return this.id;
    }
    public String getBookName(){
        return this.book;
    }

    public String getAuthor(){
        return this.author;
    }
}
