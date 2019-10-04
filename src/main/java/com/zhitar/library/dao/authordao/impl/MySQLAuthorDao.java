package com.zhitar.library.dao.authordao.impl;

import com.zhitar.library.dao.authordao.AuthorDao;
import com.zhitar.library.domain.Author;
import com.zhitar.library.sql.DaoHelper;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class MySQLAuthorDao implements AuthorDao {

    private static final Logger LOG = Logger.getLogger(MySQLAuthorDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Author.class);
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String UPDATE_QUERY = new QueryBuilder().update(TABLE, NAME_COLUMN).whereAssign(ID_COLUMN).build();
    private static final String FIND_BY_NAME_QUERY = new QueryBuilder().select().table(TABLE).whereAssign(NAME_COLUMN).build();
    private static final String FIND_ALL_QUERY = new QueryBuilder().select().table(TABLE).build();
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, NAME_COLUMN).build();
    private static final String FIND_BY_ID_QUERY = new QueryBuilder().select().table(TABLE).whereAssign(ID_COLUMN).build();
    private static final String DELETE_QUERY = new QueryBuilder().delete(TABLE).whereAssign(ID_COLUMN).build();


    public MySQLAuthorDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public Author save(Author entity) {
        LOG.info("Execute save " + entity);
        Object[] params = {entity.getName()};
        return DaoHelper.getInstance().save(INSERT_QUERY, entity, Integer.class, params);
    }

    @Override
    public List<Author> save(List<Author> authors) {
        LOG.info("Execute save with authors " + authors);
        if (authors.size() == 1) {
            return Collections.singletonList(save(authors.get(0)));
        }
        return DaoHelper.getInstance().saveBatch(
                INSERT_QUERY,
                authors,
                Integer.class,
                authors.stream().map(Author::getName).toArray()
        );
    }

    @Override
    public Author update(Author entity) {
        LOG.info("Execute update " + entity);
        Object[] params = {entity.getName(), entity.getId()};
        return DaoHelper.getInstance().update(UPDATE_QUERY, entity, params);
    }

    @Override
    public Author findById(Integer id) {
        return DaoHelper.getInstance().find(FIND_BY_ID_QUERY, this::getAuthor, id);
    }

    @Override
    public List<Author> findById(List<Integer> ids) {
        LOG.info("Execute findById with ids " + ids);
        if (ids.size() == 1) {
            return Collections.singletonList(findById(ids.get(0)));
        }
        return DaoHelper.getInstance().findList(
                new QueryBuilder().select()
                        .table(TABLE)
                        .whereIn(ID_COLUMN, ids.size())
                        .build(),
                this::getAuthor, ids.toArray());
    }

    @Override
    public Author findByName(String name) {
        return DaoHelper.getInstance().find(FIND_BY_NAME_QUERY, this::getAuthor, name);
    }

    @Override
    public List<Author> findAll() {
        return DaoHelper.getInstance().findAll(FIND_ALL_QUERY, this::getAuthor);
    }

    @Override
    public boolean delete(Integer id) {
        return DaoHelper.getInstance().delete(DELETE_QUERY, id);
    }

    private Author getAuthor(ResultSet resultSet, int row) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getInt(1));
        author.setName(resultSet.getString(2));
        return author;
    }
}