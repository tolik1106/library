package com.zhitar.library.dao.attributedao.impl;

import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.domain.Attribute;
import com.zhitar.library.sql.DaoHelper;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class MySQLAttributeDao implements AttributeDao {

    private static final Logger LOG = Logger.getLogger(MySQLAttributeDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Attribute.class);
    private static final String FIND_ALL_QUERY = new QueryBuilder().select().table(TABLE).build();
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String UPDATE_QUERY = new QueryBuilder().update(TABLE, NAME_COLUMN).whereAssign(ID_COLUMN).build();
    private static final String FIND_BY_NAME_QUERY = new QueryBuilder().select().table(TABLE).whereAssign(NAME_COLUMN).build();
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, NAME_COLUMN).build();
    private static final String FIND_BY_ID_QUERY = new QueryBuilder().select().table(TABLE).whereAssign(ID_COLUMN).build();
    private static final String DELETE_QUERY = new QueryBuilder().delete(TABLE).whereAssign(ID_COLUMN).build();

    public MySQLAttributeDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public Attribute save(Attribute entity) {
        LOG.info("Execute save with attribute " + entity);
        Object[] params = {entity.getName()};
        return DaoHelper.getInstance().save(INSERT_QUERY, entity, Integer.class, params);
    }

    @Override
    public List<Attribute> save(List<Attribute> attributes) {
        LOG.info("Execute save with attributes " + attributes);
        return DaoHelper.getInstance().saveBatch(
                INSERT_QUERY,
                attributes,
                Integer.class,
                attributes.stream().map(Attribute::getName).toArray()
        );
    }

    @Override
    public Attribute update(Attribute entity) {
        LOG.info("Execute update " + entity);
        Object[] params = {entity.getName(), entity.getId()};
        return DaoHelper.getInstance().update(UPDATE_QUERY, entity, params);
    }

    @Override
    public Attribute findById(Integer id) {
        return DaoHelper.getInstance().find(FIND_BY_ID_QUERY, this::getAttribute, id);
    }

    @Override
    public List<Attribute> findById(List<Integer> ids) {
        LOG.info("Execute findById with ids " + ids);
        if (ids.size() == 1) {
            return Collections.singletonList(findById(ids.get(0)));
        }
        return DaoHelper.getInstance().findList(
                new QueryBuilder().select()
                    .table(TABLE)
                    .whereIn(ID_COLUMN, ids.size())
                    .build(),
                this::getAttribute, ids.toArray());
    }

    @Override
    public Attribute findByAttribute(String attribute) {
        return DaoHelper.getInstance().find(FIND_BY_NAME_QUERY, this::getAttribute, attribute);
    }

    @Override
    public List<Attribute> findAll() {
        return DaoHelper.getInstance().findAll(FIND_ALL_QUERY, this::getAttribute);
    }

    @Override
    public boolean delete(Integer id) {
        return DaoHelper.getInstance().delete(DELETE_QUERY, id);
    }

    private Attribute getAttribute(ResultSet selectSet, int row) throws SQLException {
        Attribute attribute = new Attribute();
        attribute.setId(selectSet.getInt(1));
        attribute.setName(selectSet.getString(2));
        return attribute;
    }
}