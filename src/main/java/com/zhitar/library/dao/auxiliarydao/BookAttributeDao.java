package com.zhitar.library.dao.auxiliarydao;


import com.zhitar.library.domain.BookAttribute;

import java.util.List;

/**
 * Manage auxiliary {@link BookAttribute} object that
 * represent intermediate table for book and attribute
 */
public interface BookAttributeDao {

    BookAttribute save(BookAttribute bookAttribute);

    List<BookAttribute> save(List<BookAttribute> attributes);

    List<BookAttribute> findByAttributeId(Integer attributeId);

    List<BookAttribute> findByBookId(Integer bookId);

    boolean delete(Integer bookId);
}
