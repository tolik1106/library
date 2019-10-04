package com.zhitar.library.dao.bookdao.impl;

import com.zhitar.library.dao.bookdao.BookDao;
import com.zhitar.library.domain.Author;
import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.User;
import com.zhitar.library.sql.DaoHelper;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySQLBookDao implements BookDao {

    private static final Logger LOG = Logger.getLogger(MySQLBookDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Book.class);
    private static final String COUNT_QUERY = new QueryBuilder().select("count(*)").table(TABLE).build();
    private static final String ID_COLUMN = "id";
    private static final String USER_ID_COLUMN = "user_id";
    private static final String NAME_COLUMN = "name";
    private static final String OWNED_DATE_COLUMN = "owned_date";
    private static final String BOOKCASE_COLUMN = "bookcase";
    private static final String BOOKSHELF_COLUMN = "bookshelf";
    private static final String ORDERED_COLUMN = "ordered";
    private static final String FIND_BY_ID_QUERY = new QueryBuilder().select().table(TABLE).whereAssign(ID_COLUMN).build();
    private static final String FIND_BY_USER_ID_QUERY = new QueryBuilder().select()
            .table(TABLE)
            .whereAssign(USER_ID_COLUMN)
            .build();
    private static final String FIND_ALL_QUERY = new QueryBuilder().select().table(TABLE).order(NAME_COLUMN).build();
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, NAME_COLUMN, BOOKCASE_COLUMN, BOOKSHELF_COLUMN).build();
    private static final String UPDATE_QUERY = new QueryBuilder().
            update(TABLE, USER_ID_COLUMN, NAME_COLUMN, OWNED_DATE_COLUMN, BOOKCASE_COLUMN, BOOKSHELF_COLUMN, ORDERED_COLUMN)
            .whereAssign(ID_COLUMN).build();
    private static final String DELETE_QUERY = new QueryBuilder().delete(TABLE).whereAssign(ID_COLUMN).build();
    private static final int PAGE_LIMIT = 10;
    private static final String FIND_BY_NAME_WITH_AUTHOR_QUERY = new QueryBuilder()
            .select("b.id", "b.user_id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered", "a.id", "a.name")
            .table("book b")
            .join("INNER", "book_author ba", "b.id", "ba.book_id")
            .join("INNER", "author a", "ba.author_id", "a.id")
            .whereRegexp("b.name")
            .order("b.name").build();
    private static final String FIND_ALL_WITH_AUTHORS_QUERY = new QueryBuilder()
            .select("b.id", "b.user_id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered", "a.id", "a.name")
            .table("book b")
            .join("INNER", "book_author ba", "b.id", "ba.book_id")
            .join("INNER", "author a", "ba.author_id", "a.id")
            .order("b.name")
            .build();

    public MySQLBookDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public long count() {
        LOG.info("Execute count");
        return DaoHelper.getInstance().find(COUNT_QUERY, this::count);
    }

    @Override
    public Collection<Book> findByNameWithAuthor(String regexName) {
        LOG.info("Execute findByNameWithAuthor with regexName " + regexName);
        return DaoHelper.getInstance().find(FIND_BY_NAME_WITH_AUTHOR_QUERY, this::getWithAuthor, regexName);
    }

    @Override
    public Book save(Book entity) {
        LOG.info("Execute save with book " + entity);
        Object[] params = {entity.getName(), entity.getBookcase(), entity.getBookshelf()};
        return DaoHelper.getInstance().save(INSERT_QUERY, entity, Integer.class, params);
    }

    @Override
    public Book update(Book entity) {
        LOG.info("Execute update for " + entity);
        User owner = entity.getOwner();
        Object[] params = {
                owner == null ? null : owner.getId(),
                entity.getName(),
                owner == null ? null : new Date(entity.getOwnedDate().getTime()),
                entity.getBookcase(),
                entity.getBookshelf(),
                entity.getOrdered(),
                entity.getId()};
        return DaoHelper.getInstance().executeUpdate(UPDATE_QUERY, entity, params);
    }

    @Override
    public Book findById(Integer id) {
        LOG.info("Execute findById with id " + id);
        return DaoHelper.getInstance().find(FIND_BY_ID_QUERY, this::getBook, id);
    }

    @Override
    public Collection<Book> findById(List<Integer> ids) {
        LOG.info("Execute findById with ids " + ids);
        return DaoHelper.getInstance().find(
                new QueryBuilder().select("b.id", "b.user_id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered", "a.id", "a.name")
                .table("book b")
                .join("INNER", "book_author ba", "b.id", "ba.book_id")
                .join("INNER", "author a", "ba.author_id", "a.id")
                .whereIn("b.id", ids.size())
                .order("b.name")
                .build(),
                this::getWithAuthor, ids.toArray());
    }

    @Override
    public List<Book> findAll() {
        LOG.info("Execute findAll");
        return DaoHelper.getInstance().findAll(FIND_ALL_QUERY, this::getBook);
    }

    @Override
    public Collection<Book> findAllWithAuthor() {
        LOG.info("Execute findAllWithAuthor");
        return DaoHelper.getInstance().find(FIND_ALL_WITH_AUTHORS_QUERY, this::getWithAuthor);
    }

    @Override
    public Collection<Book> findAll(int page) {
        LOG.info("Execute findAll for page " + page);
        return DaoHelper.getInstance().findAll(
                new QueryBuilder().select()
                        .table(TABLE)
                        .order(NAME_COLUMN)
                        .limit(PAGE_LIMIT)
                        .offset(page * PAGE_LIMIT)
                        .build(),
                this::getBook);
    }

    @Override
    public Collection<Book> findByUser(Integer userId) {
        LOG.info("Execute findByUser with id " + userId);
        return DaoHelper.getInstance().findList(FIND_BY_USER_ID_QUERY, this::getBook, userId);
    }

    @Override
    public boolean delete(Integer id) {
        LOG.info("Execute delete with id " + id);
        return DaoHelper.getInstance().delete(DELETE_QUERY, id);

    }

    private Long count(ResultSet resultSet) throws SQLException {
        long count = 0;
        while (resultSet.next()) {
            count = resultSet.getLong(1);
        }
        return count;
    }

    private Collection<Book> getWithAuthor(ResultSet resultSet) throws SQLException {
        List<Book> books = new ArrayList<>();
        int row = 1;
        while (resultSet.next()) {
            Book book = getBook(resultSet, 1);
            if (books.contains(book)) {
                book = books.remove(books.size() - 1);
            }
            setAuthor(book, resultSet);
            books.add(book);
            LOG.trace("Book " + book);
            row++;
        }
        LOG.trace("BooksWithAuthors " + books);
        return books;
    }

    private void setAuthor(Book book, ResultSet resultSet) throws SQLException {
        Author author = new Author();
        author.setId(resultSet.getInt(8));
        author.setName(resultSet.getString(9));
        book.getAuthors().add(author);
        LOG.trace("Set Author " + author + " for book " + book);
    }

    private Book getBook(ResultSet resultSet, int row) throws SQLException {
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
}