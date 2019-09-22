package com.zhitar.library.dao.attributedao;

import com.zhitar.library.dao.GenericDao;
import com.zhitar.library.domain.Attribute;

import java.util.List;

/**
 * Manage {@link Attribute} entity
 */
public interface AttributeDao extends GenericDao<Attribute, Integer> {
    List<Attribute> save(List<Attribute> attributes);

    List<Attribute> findById(List<Integer> ids);

    Attribute findByAttribute(String attribute);
}
