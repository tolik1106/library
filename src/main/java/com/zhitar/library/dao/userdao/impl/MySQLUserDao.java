package com.zhitar.library.dao.userdao.impl;

import com.zhitar.library.dao.AbstractDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.Role;
import com.zhitar.library.domain.User;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.sql.TransactionHandler;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.*;

public class MySQLUserDao extends AbstractDao<User, Integer> implements UserDao {

    private static final Logger LOG = Logger.getLogger(MySQLUserDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(User.class);
    private static final String NAME_COLUMN = "name";
    private static final String EMAIL_COLUMN = "email";
    private static final String PHONE_COLUMN = "phone";
    private static final String ID_COLUMN = "id";

    public MySQLUserDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public User save(User entity) {
        LOG.info("Execute save with user " + entity);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, NAME_COLUMN, EMAIL_COLUMN, PHONE_COLUMN).build()
                , Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getPhone());
            LOG.trace("executeUpdate statement");
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int key = generatedKeys.getInt(1);
                LOG.trace("Generated key is " + key);
                entity.setId(key);
            }
            LOG.trace("Saved user " + entity);
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
        return entity;
    }

    @Override
    public User update(User entity) {
        LOG.info("Execute update with user " + entity);
        return super.update(entity);
    }

    @Override
    public User findById(Integer id) {
        LOG.info("Execute findById with id " + id);
        User user = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select().table(TABLE).whereAssign(ID_COLUMN).build())
        ) {
            statement.setInt(1, id);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUser(resultSet);
                LOG.trace("User " + user);
            }
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        LOG.info("Execute findByEmail with email " + email);
        User user = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder()
                        .select("u.id", "u.name", "u.email", "u.phone", "r.id", "r.role")
                        .table("users u")
                        .join("INNER", "user_role ur", "u.id", "ur.user_id")
                        .join("INNER", "role r", "ur.role_id", "r.id")
                        .whereAssign("email").build()
        )) {
            statement.setString(1, email);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                user = getUser(resultSet);
                LOG.trace("User " + user);
                setRoles(user, resultSet);
                LOG.trace("User after with roles" + user);
            }
            return user;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<User> findAll() {
        LOG.info("Execute findAll");
        try (Statement statement = TransactionHandler.getConnection().createStatement();
             ResultSet resultSet = statement.executeQuery(
                     new QueryBuilder().select().table(TABLE).build()
             )) {
            LOG.trace("executeQuery statement");
            List<User> users = new ArrayList<>();
            while (resultSet.next()) {
                User user = getUser(resultSet);
                LOG.trace("User " + user);
                users.add(user);
            }
            LOG.trace("User List " + users);
            return users;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Collection<User> findAllWithBooks() {
        LOG.info("Execute findAllWithBooks");
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select("u.id", "u.name", "u.email", "u.phone", "b.id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered")
                        .table("users u")
                        .join("INNER", "book b", "u.id", "b.user_id")
                        .order("u.name")
                        .build()
        )) {
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            Map<Integer, User> usersMap = new LinkedHashMap<>();
            while (resultSet.next()) {
                User user = getUser(resultSet);
                LOG.trace("User " + user);
                User newUser = setBook(usersMap.getOrDefault(user.getId(), user), resultSet);
                usersMap.put(user.getId(), newUser);
                LOG.trace("User with books" + newUser);
            }
            return usersMap.values();
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    private User setBook(User user, ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt(5));
        book.setName(resultSet.getString(6));
        book.setOwnedDate(resultSet.getDate(7));
        book.setBookcase(resultSet.getInt(8));
        book.setBookshelf(resultSet.getInt(9));
        book.setOrdered(resultSet.getBoolean(10));
        user.getBooks().add(book);
        return user;
    }

    private User getUser(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(1));
        user.setName(resultSet.getString(2));
        user.setEmail(resultSet.getString(3));
        user.setPhone(resultSet.getString(4));
        return user;
    }

    private void setRoles(User user, ResultSet resultSet) throws SQLException {
        do {
            Role role = new Role();
            role.setId(resultSet.getInt(5));
            role.setRole(resultSet.getString(6));
            user.getRoles().add(role);
        } while (resultSet.next());
    }

    @Override
    protected void update(User entity, Connection connection) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(
                new QueryBuilder()
                        .update(TABLE, NAME_COLUMN, EMAIL_COLUMN, PHONE_COLUMN)
                        .whereAssign(ID_COLUMN)
                        .build()
        )) {
            statement.setString(1, entity.getName());
            statement.setString(2, entity.getEmail());
            statement.setString(3, entity.getPhone());
            statement.setInt(5, entity.getId());
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
