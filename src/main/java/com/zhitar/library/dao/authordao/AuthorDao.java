package com.zhitar.library.dao.authordao;

import com.zhitar.library.dao.GenericDao;
import com.zhitar.library.domain.Author;

import java.util.List;

public interface AuthorDao extends GenericDao<Author, Integer> {
    List<Author> save(List<Author> authors);

    List<Author> findById(List<Integer> ids);

    Author findByName(String name);
}
