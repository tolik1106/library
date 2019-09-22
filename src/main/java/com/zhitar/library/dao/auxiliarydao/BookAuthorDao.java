package com.zhitar.library.dao.auxiliarydao;

import com.zhitar.library.domain.BookAuthor;

import java.util.List;

/**
 * Manage auxiliary {@link BookAuthor} object that
 * represent intermediate table for book and author
 */
public interface BookAuthorDao {

    BookAuthor save(BookAuthor bookAuthor);

    List<BookAuthor> save(List<BookAuthor> authors);

    List<BookAuthor> findByAuthorId(Integer authorId);

    List<BookAuthor> findByBookId(Integer bookId);

    boolean delete(Integer bookId);
}
