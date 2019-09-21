package com.zhitar.library.service;

import com.zhitar.library.domain.Book;

import java.util.Collection;

public interface BookService {

    Collection<Book> findByAuthor(String authorName);

    Collection<Book> findByNameWithAuthor(String name);

    Collection<Book> findByAttribute(String attribute);

    Book takeBook(Integer bookId, Integer userId);

    Collection<Book> findAll();

    Collection<Book> findAll(int page);

    long count();

    Collection<Book> findByUser(Integer userId);
}
