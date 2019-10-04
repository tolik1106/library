package com.zhitar.library.dao.auxiliarydao.impl;

import com.zhitar.library.dao.auxiliarydao.BookAuthorDao;
import com.zhitar.library.domain.BookAuthor;
import com.zhitar.library.sql.DaoHelper;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

public class MySQLBookAuthorDao implements BookAuthorDao {

    private static final Logger LOG = Logger.getLogger(MySQLBookAuthorDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(BookAuthor.class);
    private static final String BOOK_ID = "book_id";
    private static final String DELETE_QUERY = new QueryBuilder().delete(TABLE).whereAssign(BOOK_ID).build();
    private static final String AUTHOR_ID = "author_id";
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, BOOK_ID, AUTHOR_ID).build();

    public MySQLBookAuthorDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public BookAuthor save(BookAuthor bookAuthor) {
        return DaoHelper.getInstance().executeUpdate(INSERT_QUERY, bookAuthor, bookAuthor.getBookId(), bookAuthor.getAuthorId());
    }

    @Override
    public List<BookAuthor> save(List<BookAuthor> authors) {
        LOG.info("Execute save with bookAuthorList " + authors);
        if (authors.size() == 1) {
            return Collections.singletonList(save(authors.get(0)));
        }
        Object[] params = new Object[authors.size() * 2];
        int index = 0;
        for (BookAuthor author : authors) {
            params[index++] = author.getBookId();
            params[index++] = author.getAuthorId();
        }
        return DaoHelper.getInstance().saveBatch(
                INSERT_QUERY,
                authors,
                params
        );
    }

    @Override
    public List<BookAuthor> findByAuthorId(Integer authorId) {
        return findById(AUTHOR_ID, authorId);
    }

    @Override
    public List<BookAuthor> findByBookId(Integer bookId) {
        return findById(BOOK_ID, bookId);
    }

    @Override
    public boolean delete(Integer bookId) {
        LOG.info("Execute delete with bookId " + bookId);
        return DaoHelper.getInstance().delete(DELETE_QUERY, bookId);
    }

    private List<BookAuthor> findById(String column, Integer value) {
        LOG.info("Execute findById with column " + column + " and id " + value);
        return DaoHelper.getInstance().findList(
                new QueryBuilder().select().table(TABLE).whereAssign(column).build(),
                this::getBookAuthor,
                value
        );
    }

    private BookAuthor getBookAuthor(ResultSet resultSet, int row) throws SQLException {
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBookId(resultSet.getInt(1));
        bookAuthor.setAuthorId(resultSet.getInt(2));
        LOG.trace("Retrieved bookAuthor " + bookAuthor);

        return bookAuthor;
    }
}