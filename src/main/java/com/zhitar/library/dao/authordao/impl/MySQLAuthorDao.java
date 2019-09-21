package com.zhitar.library.dao.authordao.impl;

import com.zhitar.library.dao.AbstractDao;
import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.domain.Author;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.sql.TransactionHandler;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MySQLAuthorDao extends AbstractDao<Author, Integer> implements AuthorDao {

    private static final Logger LOG = Logger.getLogger(MySQLAuthorDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Author.class);
    private static final String NAME_COLUMN = "name";
    private static final String ID_COLUMN = "id";

    public MySQLAuthorDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public Author save(Author entity) {
        LOG.info("Execute save with author " + entity);

        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, NAME_COLUMN).build(),
                Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());

            LOG.trace("executeUpdate statement");

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int key = generatedKeys.getInt(1);
                LOG.trace("Generated key is " + key);
                entity.setId(key);
            }
            LOG.trace("Saved author " + entity);
            return entity;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Author> save(List<Author> authors) {
        LOG.info("Execute save with authors " + authors);
        if (authors.size() == 1) {
            return Collections.singletonList(save(authors.get(0)));
        }

        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, NAME_COLUMN).build(),
                Statement.RETURN_GENERATED_KEYS
        )) {
            LOG.trace("Prepare batch");
            for (Author author : authors) {
                statement.setString(1, author.getName());
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
                authors.get(i).setId(key);
                i++;
            }
            LOG.trace("Authors " + authors);
            return authors;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Author update(Author entity) {
        LOG.info("Execute update with author " + entity);
        return super.update(entity);
    }

    @Override
    public Author findById(Integer id) {
        LOG.info("Execute findById with id " + id);
        Author author = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select().table(TABLE).whereAssign(ID_COLUMN).build()
        )) {
            statement.setInt(1, id);
            LOG.trace("executeQuery statement");
            ResultSet selectSet = statement.executeQuery();
            if (selectSet.next()) {
                author = getAuthor(selectSet);
                LOG.trace("Author " + author);
            }
            return author;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Author> findById(List<Integer> ids) {
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
            List<Author> authors = new ArrayList<>();
            while (resultSet.next()) {
                authors.add(getAuthor(resultSet));
            }
            LOG.trace("Authors " + authors);
            return authors;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Author findByName(String name) {
        LOG.info("Execute findByName with name " + name);

        Author author = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                        .table(TABLE)
                        .whereAssign(NAME_COLUMN)
                        .build()
        )) {
            statement.setString(1, name);
            LOG.trace("executeQuery statement");
            ResultSet selectSet = statement.executeQuery();
            if (selectSet.next()) {
                author = getAuthor(selectSet);
                LOG.trace("Author " + author);
            }
            return author;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Author> findAll() {
        LOG.info("Execute findAll");
        try (Statement statement = TransactionHandler.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new QueryBuilder().select()
                             .table(TABLE)
                             .build()
             )) {
            LOG.trace("executeQuery statement");
            List<Author> authors = new ArrayList<>();
                while (resultSet.next()) {
                    Author author = getAuthor(resultSet);
                    LOG.trace("Author " + author);
                    authors.add(author);
                }
                return authors;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    private Author getAuthor(ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getInt(1));
        author.setName(resultSet.getString(2));
        return author;
    }

    @Override
    protected void update(Author entity, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                new QueryBuilder().update(TABLE, NAME_COLUMN).whereAssign(ID_COLUMN).build()
        )) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getId());
            statement.executeUpdate();
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
