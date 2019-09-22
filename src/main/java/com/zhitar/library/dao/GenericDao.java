package com.zhitar.library.dao;

import java.io.Serializable;
import java.util.List;

/**
 * Root interface for all dao
 * @param <T> entity
 * @param <ID> identifier
 */
public interface GenericDao<T, ID extends Serializable> {

    T save(T entity);

    T update(T entity);

    boolean delete(ID id);

    T findById(ID id);

    List<T> findAll();
}
