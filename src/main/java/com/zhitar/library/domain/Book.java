package com.zhitar.library.domain;

import com.zhitar.library.validation.annotation.Length;
import com.zhitar.library.validation.annotation.Range;

import java.util.*;

public class Book extends AbstractEntity<Integer> {
    private static final long serialVersionUID = 1L;

    @Length(min = 3, max = 100, message = "message.booknamelength.error")
    private String name;

    private List<Author> authors = new ArrayList<>();

    private User owner;

    private Date ownedDate;

    private List<Attribute> attributes = new ArrayList<>();

    @Range(max = 20, message = "message.bookcase.error")
    private Integer bookcase;

    @Range(message = "message.bookshelf.error")
    private Integer bookshelf;

    public Book() {
    }

    public Book(Integer id, String name, Date ownedDate, Integer bookcase, Integer bookshelf) {
        super(id);
        this.name = name;
        this.ownedDate = ownedDate;
        this.bookcase = bookcase;
        this.bookshelf = bookshelf;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Author> getAuthors() {
        return authors;
    }

    public void setAuthors(List<Author> authors) {
        this.authors = authors;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Date getOwnedDate() {
        return ownedDate;
    }

    public void setOwnedDate(Date ownedDate) {
        this.ownedDate = ownedDate;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<Attribute> attributes) {
        this.attributes = attributes;
    }

    public Integer getBookcase() {
        return bookcase;
    }

    public void setBookcase(Integer bookcase) {
        this.bookcase = bookcase;
    }

    public Integer getBookshelf() {
        return bookshelf;
    }

    public void setBookshelf(Integer bookshelf) {
        this.bookshelf = bookshelf;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", ownedDate=" + ownedDate +
                ", bookcase=" + bookcase +
                ", bookshelf=" + bookshelf +
                '}';
    }
}
