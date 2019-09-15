package com.zhitar.library.dao.auxiliarydao.impl;

import com.zhitar.library.dao.auxiliarydao.BookAuthorDAO;
import com.zhitar.library.domain.BookAuthor;
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

public class MySQLBookAuthorDao implements BookAuthorDAO {

    private static final Logger LOG = Logger.getLogger(MySQLBookAuthorDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(BookAuthor.class);
    private static final String BOOK_ID = "book_id";
    private static final String AUTHOR_ID = "author_id";

    public MySQLBookAuthorDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public BookAuthor save(BookAuthor bookAuthor) {
        LOG.info("Execute save with bookAuthor " + bookAuthor);

        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, BOOK_ID, AUTHOR_ID).build()
        )) {
            statement.setInt(1, bookAuthor.getBookId());
            statement.setInt(2, bookAuthor.getAuthorId());
            LOG.trace("executeUpdate statement");
            statement.executeUpdate();
            return bookAuthor;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<BookAuthor> save(List<BookAuthor> authors) {
        LOG.info("Execute save with bookAuthorList " + authors);
        if (authors.size() == 1) {
            return Collections.singletonList(save(authors.get(0)));
        }
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, BOOK_ID, AUTHOR_ID).build()
        )) {
            LOG.trace("Prepare batch");
            for (BookAuthor bookAuthor : authors) {
                statement.setInt(1, bookAuthor.getBookId());
                statement.setInt(2, bookAuthor.getAuthorId());
                statement.addBatch();
            }
            LOG.trace("executeBatch statement");
            statement.executeBatch();
            return authors;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
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
            throw new DaoException(e.getMessage(), e);
        }
    }

    private List<BookAuthor> findById(String column, Integer value) {
        LOG.info("Execute findById with column " + column + " and id " + value);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select().table(TABLE).whereAssign(column).build()
        )) {
            statement.setInt(1, value);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            return getBookAuthors(resultSet);
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    private List<BookAuthor> getBookAuthors(ResultSet resultSet) throws SQLException {
        List<BookAuthor> bookAuthors = new ArrayList<>();
        while (resultSet.next()) {
            bookAuthors.add(getBookAuthor(resultSet));
        }
        LOG.trace("Retrieved bookAuthors " + bookAuthors);

        return bookAuthors;
    }

    private BookAuthor getBookAuthor(ResultSet resultSet) throws SQLException {
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBookId(resultSet.getInt(1));
        bookAuthor.setAuthorId(resultSet.getInt(2));
        LOG.trace("Retrieved bookAuthor " + bookAuthor);

        return bookAuthor;
    }
}
