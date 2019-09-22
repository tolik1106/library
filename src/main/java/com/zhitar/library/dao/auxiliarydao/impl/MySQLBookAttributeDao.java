package com.zhitar.library.dao.auxiliarydao.impl;

import com.zhitar.library.dao.auxiliarydao.BookAttributeDao;
import com.zhitar.library.domain.BookAttribute;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.sql.TransactionHandler;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySQLBookAttributeDao implements BookAttributeDao {

    private static final Logger LOG = Logger.getLogger(MySQLBookAttributeDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(BookAttribute.class);
    private static final String BOOK_ID = "book_id";
    private static final String ATTRIBUTE_ID = "attribute_id";

    public MySQLBookAttributeDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public BookAttribute save(BookAttribute bookAttribute) {
        LOG.info("Execute save with bookAttribute " + bookAttribute);

        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, BOOK_ID, ATTRIBUTE_ID).build()
        )) {
            statement.setInt(1, bookAttribute.getBookId());
            statement.setInt(2, bookAttribute.getAttributeId());
            LOG.trace("executeUpdate statement");
            statement.executeUpdate();
            return bookAttribute;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<BookAttribute> save(List<BookAttribute> attributes) {
        LOG.info("Execute save with bookAttributeList " + attributes);
        if (attributes.size() == 1) {
            return Collections.singletonList(save(attributes.get(0)));
        }
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, BOOK_ID, ATTRIBUTE_ID).build()
        )) {
            LOG.trace("Prepare batch");
            for (BookAttribute bookAttribute : attributes) {
                statement.setInt(1, bookAttribute.getBookId());
                statement.setInt(2, bookAttribute.getAttributeId());
                statement.addBatch();
            }
            LOG.trace("executeBatch statement");
            statement.executeBatch();
            return attributes;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
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
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().delete(TABLE).whereAssign(BOOK_ID).build()
        )) {
            statement.setInt(1, bookId);
            LOG.trace("executeUpdate statement");
            boolean result = statement.executeUpdate() != 0;
            LOG.trace("Result: " + result);
            return result;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    private List<BookAttribute> findById(String column, Integer value) {
        LOG.info("Execute findById with column " + column + " and id " + value);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select().table(TABLE).whereAssign(column).build()
        )) {
            statement.setInt(1, value);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            return getBookAttributes(resultSet);
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    private List<BookAttribute> getBookAttributes(ResultSet resultSet) throws SQLException {
        List<BookAttribute> bookAttributes = new ArrayList<>();
        while (resultSet.next()) {
            bookAttributes.add(getBookAttribute(resultSet));
        }
        LOG.trace("Retrieved bookAttributes " + bookAttributes);
        return bookAttributes;
    }

    private BookAttribute getBookAttribute(ResultSet resultSet) throws SQLException {
        BookAttribute bookAttribute = new BookAttribute();
        bookAttribute.setAttributeId(resultSet.getInt(1));
        bookAttribute.setBookId(resultSet.getInt(2));
        LOG.trace("Retrieved bookAttribute " + bookAttribute);

        return bookAttribute;
    }
}
