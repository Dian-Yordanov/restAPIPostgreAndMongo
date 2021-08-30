package com.example.springboot;

import java.util.ArrayList;

public class Book {
    String id;
    String book;
    String author;

    /**
     * Book' name and authors mapping that is used to input/read data from PostgreDB and make a mapping with the "Author" object.
     * Different from Author not only in mapping order but also in the ability to get ID.
     */
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
