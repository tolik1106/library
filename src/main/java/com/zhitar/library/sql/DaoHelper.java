package com.zhitar.library.sql;

import com.zhitar.library.domain.AbstractEntity;
import com.zhitar.library.exception.DaoException;
import org.apache.log4j.Logger;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DaoHelper {

    private static final Logger LOG = Logger.getLogger(DaoHelper.class.getName());

    private DaoHelper() {}

    public static DaoHelper getInstance() {
        return DaoHelperHolder.INSTANCE;
    }

    // Save entity
    public <T extends AbstractEntity<PK>, PK extends Serializable> T save(String query, T entity, Class<PK> pkClass, Object... params) {
        LOG.info("Execute save with entity " + entity);
        try (PreparedStatement statement = TransactionHandler.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)
        ) {
            fillPreparedStatement(statement, params);
            LOG.trace("executeUpdate statement");
            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                PK key = getGeneratedKey(generatedKeys, pkClass);
                LOG.trace("Generated key is " + key);
                entity.setId(key);
            }
            LOG.trace("Saved entity " + entity);
            return entity;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    public <T extends AbstractEntity<PK>, PK extends Serializable> List<T> saveBatch(String query, List<T> entities, Class<PK> pkClass, Object... params) {
        LOG.info("Execute saveBatch with entities " + entities);
        try (PreparedStatement statement = TransactionHandler.getConnection()
                .prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            LOG.trace("Prepare batch");
            for (int i = 0; i < params.length; i++) {
                fillPreparedStatement(statement, params[i]);
                statement.addBatch();
            }
            LOG.trace("executeBatch statement");
            statement.executeBatch();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            int i = 0;
            PK key;
            while (generatedKeys.next()) {
                key = getGeneratedKey(generatedKeys, pkClass);
                entities.get(i).setId(key);
                i++;
            }
            LOG.trace("Entities " + entities);
            return entities;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    // Save auxiliary
    public <T> T save(String query, T entity, Object... params) {
        LOG.info("Execute save with entity " + entity);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(query)) {
            fillPreparedStatement(statement, params);
            LOG.trace("executeUpdate statement");
            statement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    public <T> List<T> saveBatch(String query, List<T> list, Object... params) {
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(query)) {
            LOG.trace("Prepare batch");
            for (int i = 0; i < params.length; i += 2) {
                fillPreparedStatement(statement, params[i], params[i + 1]);
                statement.addBatch();
            }
            LOG.trace("executeBatch statement");
            statement.executeBatch();
            return list;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    public <T> T update(String query, T entity, Object... params) {
        LOG.info("Execute update with entity " + entity);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(query)) {
            fillPreparedStatement(statement, params);
            statement.executeUpdate();
            LOG.trace("Update was successful");
            return entity;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    public boolean delete(String query, Object... params) {
        LOG.info("Execute delete with params " + Arrays.toString(params));
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(query)) {
            fillPreparedStatement(statement, params);
            LOG.trace("executeUpdate statement");
            boolean result = statement.executeUpdate() != 0;
            LOG.trace("Result is " + result);
            return result;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    public <T> T find(String query, RowMapper<T> rowMapper, Object... params) {
        LOG.info("Execute find with params: " + Arrays.toString(params));
        T entity = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(query)) {
            fillPreparedStatement(statement, params);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                entity = rowMapper.mapRow(resultSet, 1);
                LOG.trace("Entity " + entity);
            }
            return entity;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    public <T> List<T> findAll(String query, RowMapper<T> rowMapper) {
        LOG.info("Execute findAll");
        try (Statement statement = TransactionHandler.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(query)
        ) {
            LOG.trace("executeQuery statement");
            List<T> entities = new ArrayList<>();
            int row = 1;
            while (resultSet.next()) {
                T entity = rowMapper.mapRow(resultSet, row);
                LOG.trace("Entity " + entity);
                entities.add(entity);
                row++;
            }
            LOG.trace("Entity List " + entities);
            return entities;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    public <T> T find(String query, ResultSetExtractor<T> extractor, Object... params) {
        LOG.info("Execute find");
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(query)) {
            LOG.trace("executeQuery statement");
            fillPreparedStatement(statement, params);
            ResultSet resultSet = statement.executeQuery();
            return extractor.extract(resultSet);
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    public <T> List<T> findList(String query, RowMapper<T> rowMapper, Object... params) {
        LOG.info("Execute findList");
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(query)) {
            LOG.trace("executeQuery statement");
            fillPreparedStatement(statement, params);
            ResultSet resultSet = statement.executeQuery();
            List<T> entities = new ArrayList<>();
            int row = 1;
            while (resultSet.next()) {
                T entity = rowMapper.mapRow(resultSet, row);
                LOG.trace("Entity " + entity);
                entities.add(entity);
                row++;
            }
            LOG.trace("Entity List " + entities);
            return entities;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    private void fillPreparedStatement(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }

    private <PK> PK getGeneratedKey(ResultSet resultSet, Class<PK> pkClass) throws SQLException {
        return resultSet.getObject(1, pkClass);
    }

    private static class DaoHelperHolder {
        private static final DaoHelper INSTANCE = new DaoHelper();
    }
}
