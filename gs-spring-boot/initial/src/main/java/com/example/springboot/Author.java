package com.example.springboot;

public class Author {

    String book;
    String author;

    /**
     * Authors' name and books mapping that is used to input/read data from MongoDB and make a mapping with the "Book" object.
     */
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
