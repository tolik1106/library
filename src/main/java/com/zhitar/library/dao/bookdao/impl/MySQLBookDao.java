package com.zhitar.library.dao.bookdao.impl;

import com.zhitar.library.dao.AbstractDao;
import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.domain.Author;
import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.sql.TransactionHandler;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.*;
import java.sql.Date;
import java.util.*;

public class MySQLBookDao extends AbstractDao<Book, Integer> implements BookDao {

    private static final Logger LOG = Logger.getLogger(MySQLBookDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Book.class);
    private static final String ID_COLUMN = "id";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String NAME_COLUMN = "name";
    private static final String OWNED_DATE_COLUMN = "owned_date";
    private static final String BOOKCASE_COLUMN = "bookcase";
    private static final String BOOKSHELF_COLUMN = "bookshelf";
    private static final String ORDERED_COLUMN = "ordered";
    private static final String ERROR_MESSAGE = "An error occurred during execution";

    public MySQLBookDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public long count() {
        LOG.info("Execute count");
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select("count(*)").table(TABLE).build()
        )) {
            ResultSet resultSet = statement.executeQuery();
            long count = 0;
            while (resultSet.next()) {
                count = resultSet.getLong(1);
            }
            return count;
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }
    @Override
    public Collection<Book> findByNameWithAuthor(String regexName) {
        LOG.info("Execute findByNameWithAuthor with regexName " + regexName);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select("b.id", "b.user_id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered", "a.id", "a.name")
                        .table("book b")
                        .join("INNER", "book_author ba", "b.id", "ba.book_id")
                        .join("INNER", "author a", "ba.author_id", "a.id")
                        .whereRegexp("b.name")
                        .order("b.name").build()
        )) {
            statement.setString(1, regexName);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            return getWithAuthor(resultSet);
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Book save(Book entity) {
        LOG.info("Execute save with book " + entity);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, NAME_COLUMN, BOOKCASE_COLUMN, BOOKSHELF_COLUMN).build(),
                Statement.RETURN_GENERATED_KEYS
        )) {
            statement.setString(1, entity.getName());
            statement.setInt(2, entity.getBookcase());
            statement.setInt(3, entity.getBookshelf());

            LOG.trace("executeUpdate statement");

            statement.executeUpdate();
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int key = generatedKeys.getInt(1);
                LOG.trace("Generated key is " + key);
                entity.setId(key);
            }
            LOG.trace("Saved book " + entity);
            return entity;
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Book update(Book entity) {
        LOG.info("Execute update with book " + entity);
        return super.update(entity);
    }


    @Override
    public Book findById(Integer id) {
        LOG.info("Execute findById with id " + id);
        Book book = null;
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
                book = getBook(selectSet);
                LOG.trace("Book " + book);
            }
            return book;
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Collection<Book> findById(List<Integer> ids) {
        LOG.info("Execute findById with ids " + ids);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select("b.id", "b.user_id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered", "a.id", "a.name")
                .table("book b")
                .join("INNER", "book_author ba", "b.id", "ba.book_id")
                .join("INNER", "author a", "ba.author_id", "a.id")
                .whereIn("b.id", ids.size())
                .order("b.name")
                .build()
        )) {
            for (int i = 0; i < ids.size(); i++) {
                statement.setInt(i + 1, ids.get(i));
            }
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            return getWithAuthor(resultSet);
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Book> findAll() {
        LOG.info("Execute findAll");
        try (Statement statement = TransactionHandler.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new QueryBuilder().select().table(TABLE).order(NAME_COLUMN).build()
             )) {
            LOG.trace("executeQuery statement");
            return getBooks(resultSet);
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Collection<Book> findAllWithAuthor() {
        LOG.info("Execute findAllWithAuthor");
        try (Statement statement = TransactionHandler.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new QueryBuilder().select("b.id", "b.user_id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered", "a.id", "a.name")
                     .table("book b")
                     .join("INNER", "book_author ba", "b.id", "ba.book_id")
                     .join("INNER", "author a", "ba.author_id", "a.id")
                     .order("b.name")
                     .build()
             )) {
            LOG.trace("executeQuery statement");
            return getWithAuthor(resultSet);
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Collection<Book> findAll(int page) {
        LOG.info("Execute findAll for page " + page);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                        .table(TABLE)
                        .order(NAME_COLUMN)
                        .limit(10)
                        .offset(page * 10)
                        .build()
        )) {
            ResultSet resultSet = statement.executeQuery();
            return getBooks(resultSet);
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Collection<Book> findByUser(Integer userId) {
        LOG.info("Execute findByUser with id " + userId);

        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                        .table(TABLE)
                        .whereAssign(USER_ID_COLUMN)
                        .build()
        )) {
            statement.setInt(1, userId);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            return getBooks(resultSet);
        } catch (SQLException e) {
            LOG.error(ERROR_MESSAGE, e);
            throw new DaoException(e.getMessage());
        }
    }

    private Collection<Book> getWithAuthor(ResultSet resultSet) throws SQLException {
        Map<Integer, Book> booksMap = new LinkedHashMap<>();
        while (resultSet.next()) {
            Book book = getBook(resultSet);
            Book newBook = setAuthor(booksMap.getOrDefault(book.getId(), book), resultSet);
            booksMap.put(book.getId(), newBook);
            LOG.trace("Book " + newBook);
        }
        LOG.trace("BooksWithAuthors " + booksMap.values());
        return booksMap.values();
    }

    private Book setAuthor(Book book, ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getInt(8));
        author.setName(resultSet.getString(9));
        book.getAuthors().add(author);
        LOG.trace("Set Author " + author + " for book " + book);

        return book;
    }

    private List<Book> getBooks(ResultSet resultSet) throws SQLException {
        List<Book> books = new ArrayList<>();
        while (resultSet.next()) {
            Book book = getBook(resultSet);
            books.add(book);
        }
        LOG.trace("Book List " + books);
        return books;
    }

    private Book getBook(ResultSet resultSet) throws SQLException {
        LOG.trace("Retrieving book");
        Book book = new Book();
        book.setId(resultSet.getInt(ID_COLUMN));
        book.setName(resultSet.getString(NAME_COLUMN));
        book.setOwnedDate(resultSet.getDate(OWNED_DATE_COLUMN));
        book.setBookcase(resultSet.getInt(BOOKCASE_COLUMN));
        book.setBookshelf(resultSet.getInt(BOOKSHELF_COLUMN));
        book.setOrdered(resultSet.getBoolean(ORDERED_COLUMN));
        LOG.trace("Retrieved book " + book);
        return book;
    }

    @Override
    protected void update(Book entity, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                new QueryBuilder().update(TABLE, USER_ID_COLUMN, NAME_COLUMN, OWNED_DATE_COLUMN, BOOKCASE_COLUMN, BOOKSHELF_COLUMN, ORDERED_COLUMN)
                .whereAssign(ID_COLUMN).build())) {
            User owner = entity.getOwner();
            if (owner != null) {
                statement.setInt(1, owner.getId());
            } else {
                statement.setObject(1, null);
            }
            statement.setString(2, entity.getName());
            statement.setDate(3, owner == null ? null : new Date(entity.getOwnedDate().getTime()));
            statement.setInt(4, entity.getBookcase());
            statement.setInt(5, entity.getBookshelf());
            statement.setBoolean(6, entity.getOrdered());
            statement.setInt(7, entity.getId());

            LOG.trace("executeUpdate statement");

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
