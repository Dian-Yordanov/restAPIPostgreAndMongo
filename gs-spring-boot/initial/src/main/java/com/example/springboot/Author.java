package com.example.springboot;

public class Author {

    String book;
    String author;

    public Author(String author, String book) {

        this.book = book;
        this.author = author;

    }

    public String getBookName(){
        return this.book;
    }

    public String getAuthor(){
        return this.author;
    }

}
