package com.zhitar.library.dao.auxiliarydao.impl;

import com.zhitar.library.dao.auxiliarydao.BookAttributeDao;
import com.zhitar.library.domain.BookAttribute;
import com.zhitar.library.sql.DaoHelper;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class MySQLBookAttributeDao implements BookAttributeDao {

    private static final Logger LOG = Logger.getLogger(MySQLBookAttributeDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(BookAttribute.class);
    private static final String BOOK_ID = "book_id";
    private static final String DELETE_QUERY = new QueryBuilder().delete(TABLE).whereAssign(BOOK_ID).build();
    private static final String ATTRIBUTE_ID = "attribute_id";
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, BOOK_ID, ATTRIBUTE_ID).build();

    public MySQLBookAttributeDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public BookAttribute save(BookAttribute bookAttribute) {
        return DaoHelper.getInstance().executeUpdate(INSERT_QUERY, bookAttribute, bookAttribute.getBookId(), bookAttribute.getAttributeId());
    }

    @Override
    public List<BookAttribute> save(List<BookAttribute> attributes) {
        LOG.info("Execute save with bookAttributeList " + attributes);
        if (attributes.size() == 1) {
            return Collections.singletonList(save(attributes.get(0)));
        }
        Object[] params = new Object[attributes.size() * 2];
        int index = 0;
        for (BookAttribute attribute : attributes) {
            params[index++] = attribute.getBookId();
            params[index++] = attribute.getAttributeId();
        }
        return DaoHelper.getInstance().saveBatch(
                INSERT_QUERY,
                attributes,
                params
        );
    }

    @Override
    public List<BookAttribute> findByAttributeId(Integer attributeId) {
        return findById(ATTRIBUTE_ID, attributeId);
    }

    @Override
    public List<BookAttribute> findByBookId(Integer bookId) {
        return findById(BOOK_ID, bookId);
    }

    @Override
    public boolean delete(Integer bookId) {
        LOG.info("Execute delete with bookId " + bookId);
        return DaoHelper.getInstance().delete(DELETE_QUERY, bookId);
    }

    private List<BookAttribute> findById(String column, Integer value) {
        LOG.info("Execute findById with column " + column + " and id " + value);
        return DaoHelper.getInstance().findList(
                new QueryBuilder().select().table(TABLE).whereAssign(column).build(),
                this::getBookAttribute,
                value
        );
    }

    private BookAttribute getBookAttribute(ResultSet resultSet, int row) throws SQLException {
        BookAttribute bookAttribute = new BookAttribute();
        bookAttribute.setAttributeId(resultSet.getInt(1));
        bookAttribute.setBookId(resultSet.getInt(2));
        LOG.trace("Retrieved bookAttribute " + bookAttribute);

        return bookAttribute;
    }
}