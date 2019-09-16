package com.zhitar.library.service;

import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;

import java.util.Collection;

public interface AdminService {

    boolean deleteBookById(Integer id);

    Book findBookById(Integer id);

    Book save(Book book);

    Book update(Book book);

    Collection<User> findUsersWithBook();

    Book returnBook(Integer bookId, Integer userId);
}
