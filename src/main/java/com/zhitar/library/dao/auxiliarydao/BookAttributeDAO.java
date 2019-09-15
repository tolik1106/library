package com.zhitar.library.dao.auxiliarydao;


import com.zhitar.library.domain.BookAttribute;

import java.util.List;

public interface BookAttributeDAO {

    BookAttribute save(BookAttribute bookAttribute);

    List<BookAttribute> save(List<BookAttribute> attributes);

    List<BookAttribute> findByAttributeId(Integer attributeId);

    List<BookAttribute> findByBookId(Integer bookId);

    boolean delete(Integer bookId);
}
