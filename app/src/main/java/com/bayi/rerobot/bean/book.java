package com.bayi.rerobot.bean;

/**
 * Created by tongzn
 * on 2021/4/17
 */
public class book {
    private String book_type,book_name,book_author,book_publisher;
    private String book_publish_date="";
    private String marcno;

    public String getMarcno() {
        return marcno;
    }

    public void setMarcno(String marcno) {
        this.marcno = marcno;
    }

    public String getBook_type() {
        return book_type;
    }

    public void setBook_type(String book_type) {
        this.book_type = book_type;
    }

    public String getBook_name() {
        return book_name;
    }

    public void setBook_name(String book_name) {
        this.book_name = book_name;
    }

    public String getBook_author() {
        return book_author;
    }

    public void setBook_author(String book_author) {
        this.book_author = book_author;
    }

    public String getBook_publisher() {
        return book_publisher;
    }

    public void setBook_publisher(String book_publisher) {
        this.book_publisher = book_publisher;
    }

    public String getBook_publish_date() {
        return book_publish_date;
    }

    public void setBook_publish_date(String book_publish_date) {
        this.book_publish_date = book_publish_date;
    }
}
