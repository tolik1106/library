package com.zhitar.library.dao.attributedao.impl;

import com.zhitar.library.dao.AbstractDao;
import com.zhitar.library.dao.attributedao.AttributeDao;
import com.zhitar.library.domain.Attribute;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.sql.TransactionHandler;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySQLAttributeDao extends AbstractDao<Attribute, Integer> implements AttributeDao {

    private static final Logger LOG = Logger.getLogger(MySQLAttributeDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Attribute.class);
    private static final String NAME_COLUMN = "name";
    private static final String ID_COLUMN = "id";

    public MySQLAttributeDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public Attribute save(Attribute entity) {
        LOG.info("Execute save with attribute " + entity);

        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, NAME_COLUMN).build(),
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, entity.getName());
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int key = generatedKeys.getInt(1);
                LOG.trace("Generated key is " + key);
                entity.setId(key);
            }
            LOG.trace("Saved attribute " + entity);
            return entity;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Attribute> save(List<Attribute> attributes) {
        LOG.info("Execute save with attributes " + attributes);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, NAME_COLUMN).build(),
                Statement.RETURN_GENERATED_KEYS
        )) {
            LOG.trace("Prepare batch");
            for (Attribute attribute : attributes) {
                statement.setString(1, attribute.getName());
                statement.addBatch();
            }
            LOG.trace("executeBatch statement");
            statement.executeBatch();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int i = 0;
            int key;
            while (generatedKeys.next()) {
                key = generatedKeys.getInt(1);
                LOG.trace("Generated key: " + key);
                attributes.get(i).setId(key);
                i++;
            }
            LOG.trace("Attributes " + attributes);
            return attributes;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Attribute update(Attribute entity) {
        LOG.info("Execute update with attribute " + entity);
        return super.update(entity);
    }

    @Override
    public Attribute findById(Integer id) {
        LOG.info("Execute findById with id " + id);
        Attribute attribute = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                    .table(TABLE)
                    .whereAssign(ID_COLUMN)
                    .build()
        )) {
            statement.setInt(1, id);
            LOG.trace("executeQuery statement");
            ResultSet selectSet = statement.executeQuery();
            if (selectSet.next()) {
                attribute = getAttribute(selectSet);
                LOG.trace("Attribute " + attribute);
            }
            return attribute;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Attribute> findById(List<Integer> ids) {
        LOG.info("Execute findById with ids " + ids);

        if (ids.size() == 1) {
            return Collections.singletonList(findById(ids.get(0)));
        }
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                    .table(TABLE)
                    .whereIn(ID_COLUMN, ids.size())
                    .build()
        )) {
            for (int i = 1; i <= ids.size(); i++) {
                statement.setInt(i, ids.get(i - 1));
            }
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            List<Attribute> attributes = new ArrayList<>();
            while (resultSet.next()) {
                attributes.add(getAttribute(resultSet));
            }
            LOG.trace("Attributes " + attributes);
            return attributes;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Attribute findByAttribute(String attribute) {
        LOG.info("Execute findByAttribute with attribute " + attribute);

        Attribute bookAttribute = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                    .table(TABLE)
                    .whereAssign(NAME_COLUMN)
                    .build()
        )) {
            statement.setString(1, attribute);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                bookAttribute = getAttribute(resultSet);
                LOG.trace("Attribute " + bookAttribute);
            }
            return bookAttribute;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Attribute> findAll() {
        LOG.info("Execute findAll");
        try (Statement statement = TransactionHandler.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new QueryBuilder().select().table(TABLE).build()
             )) {
            LOG.trace("executeQuery statement");
                List<Attribute> attributes = new ArrayList<>();
                while (resultSet.next()){
                    Attribute attribute = getAttribute(resultSet);
                    LOG.trace("Attribute " + attribute);
                    attributes.add(attribute);
                }
                return attributes;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    private Attribute getAttribute(ResultSet selectSet) throws SQLException {
        Attribute attribute = new Attribute();
        attribute.setId(selectSet.getInt(1));
        attribute.setName(selectSet.getString(2));
        return attribute;
    }

    @Override
    protected void update(Attribute entity, Connection connection) throws SQLException {
        try (PreparedStatement updateStatement = connection.prepareStatement(
                new QueryBuilder().update(TABLE, NAME_COLUMN).whereAssign(ID_COLUMN).build()
        )) {
            updateStatement.setString(1, entity.getName());
            updateStatement.executeUpdate();
        }
    }

    @Override
    protected String getTableName() {
        return TABLE;
    }

    @Override
    protected String getIdColumnName() {
        return ID_COLUMN;
    }
}
