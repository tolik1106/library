package com.zhitar.library.dao;

import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.sql.TransactionHandler;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractDao<T, ID extends Serializable> {

    private static final Logger LOG = Logger.getLogger(AbstractDao.class.getName());

    public T update(T entity) {
        try {
            update(entity, TransactionHandler.getConnection());
            LOG.trace("Update was successful");
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
        return entity;
    }

    public boolean delete(ID id) {
        LOG.info("Execute delete with id = " + id);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().delete(getTableName())
                        .whereAssign(getIdColumnName())
                        .build()
        )) {
            statement.setObject(1, id);
            LOG.trace("executeUpdate statement");
            boolean result = statement.executeUpdate() != 0;
            LOG.trace("Result is " + result);
            return result;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
    }
    
    protected abstract void update(T entity, Connection connection) throws SQLException;

    protected abstract String getTableName();
    protected abstract String getIdColumnName();
}
