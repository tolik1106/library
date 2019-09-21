package com.zhitar.library.dao.bookdao;

import com.zhitar.library.dao.GenericDao;
import com.zhitar.library.domain.Book;

import java.util.Collection;
import java.util.List;

public interface BookDao extends GenericDao<Book, Integer> {

    Collection<Book> findById(List<Integer> ids);

    long count();

    Collection<Book> findByNameWithAuthor(String regexName);

    Collection<Book> findAllWithAuthor();

    Collection<Book> findAll(int page);

    Collection<Book> findByUser(Integer userId);
}
