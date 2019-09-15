package com.zhitar.library.dao;

import java.io.Serializable;
import java.util.List;

public interface GenericDao<T, ID extends Serializable> {

    T save(T entity);

    T update(T entity);

    boolean delete(ID id);

    T findById(ID id);

    List<T> findAll();
}
