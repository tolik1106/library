package com.zhitar.library.dao.auxiliarydao;

import com.zhitar.library.domain.BookAuthor;

import java.util.List;

public interface BookAuthorDAO {

    BookAuthor save(BookAuthor bookAuthor);

    List<BookAuthor> save(List<BookAuthor> authors);

    List<BookAuthor> findByAuthorId(Integer authorId);

    List<BookAuthor> findByBookId(Integer bookId);

    boolean delete(Integer bookId);
}
