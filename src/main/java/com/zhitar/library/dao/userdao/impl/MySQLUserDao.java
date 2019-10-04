package com.zhitar.library.dao.userdao.impl;

import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.Book;
import com.zhitar.library.domain.Role;
import com.zhitar.library.domain.User;
import com.zhitar.library.sql.DaoHelper;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MySQLUserDao implements UserDao {

    private static final Logger LOG = Logger.getLogger(MySQLUserDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(User.class);
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String EMAIL_COLUMN = "email";
    private static final String PASSWORD_COLUMN = "password";
    private static final String PHONE_COLUMN = "phone";
    private static final String UPDATE_QUERY = new QueryBuilder()
            .update(TABLE, NAME_COLUMN, EMAIL_COLUMN, PASSWORD_COLUMN, PHONE_COLUMN)
            .whereAssign(ID_COLUMN)
            .build();
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, NAME_COLUMN, EMAIL_COLUMN, PASSWORD_COLUMN, PHONE_COLUMN).build();
    private static final String FIND_BY_ID_QUERY = new QueryBuilder().select().table(TABLE).whereAssign(ID_COLUMN).build();
    private static final String FIND_ALL_QUERY = new QueryBuilder().select().table(TABLE).build();
    private static final String DELETE_QUERY = new QueryBuilder().delete(TABLE).whereAssign(ID_COLUMN).build();
    private static final String FIND_BY_EMAIL_WITH_ROLES_QUERY = new QueryBuilder()
            .select("u.id", "u.name", "u.email", "u.password", "u.phone", "r.id", "r.role")
            .table("users u")
            .join("INNER", "user_role ur", "u.id", "ur.user_id")
            .join("INNER", "role r", "ur.role_id", "r.id")
            .whereAssign("email").build();
    private static final String FIND_ALL_WITH_BOOKS_QUERY = new QueryBuilder()
            .select("u.id", "u.name", "u.email", "u.password", "u.phone", "b.id", "b.name", "b.owned_date", "b.bookcase", "b.bookshelf", "b.ordered")
            .table("users u")
            .join("INNER", "book b", "u.id", "b.user_id")
            .order("u.name", "b.name")
            .build();

    public MySQLUserDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public User save(User entity) {
        Object[] params = {entity.getName(), entity.getEmail(), entity.getPassword(), entity.getPhone()};
        return DaoHelper.getInstance().save(INSERT_QUERY, entity, Integer.class, params);
    }

    @Override
    public User update(User entity) {
        Object[] params = {
                entity.getName(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getPhone(),
                entity.getId()
        };
        return DaoHelper.getInstance().update(UPDATE_QUERY, entity, params);
    }

    @Override
    public User findById(Integer id) {
        return DaoHelper.getInstance().find(FIND_BY_ID_QUERY, this::getUser, id);
    }

    @Override
    public User findByEmail(String email) {
        return DaoHelper.getInstance().find(FIND_BY_EMAIL_WITH_ROLES_QUERY, this::getUserWithRoles, email);
    }

    @Override
    public List<User> findAll() {
        return DaoHelper.getInstance().findAll(FIND_ALL_QUERY, this::getUser);
    }

    @Override
    public Collection<User> findAllWithBooks() {
        return DaoHelper.getInstance().find(FIND_ALL_WITH_BOOKS_QUERY, this::getAllWithBooks);
    }

    @Override
    public boolean delete(Integer id) {
        return DaoHelper.getInstance().delete(DELETE_QUERY, id);
    }

    private void setBook(User user, ResultSet resultSet) throws SQLException {
        Book book = new Book();
        book.setId(resultSet.getInt(6));
        book.setName(resultSet.getString(7));
        book.setOwnedDate(resultSet.getDate(8));
        book.setBookcase(resultSet.getInt(9));
        book.setBookshelf(resultSet.getInt(10));
        book.setOrdered(resultSet.getBoolean(11));
        user.getBooks().add(book);
    }

    private User getUser(ResultSet resultSet, int row) throws SQLException {
        User user = new User();
        user.setId(resultSet.getInt(1));
        user.setName(resultSet.getString(2));
        user.setEmail(resultSet.getString(3));
        user.setPassword(resultSet.getString(4));
        user.setPhone(resultSet.getString(5));
        return user;
    }

    private void setRoles(User user, ResultSet resultSet) throws SQLException {
            Role role = new Role();
            role.setId(resultSet.getInt(6));
            role.setRole(resultSet.getString(7));
            user.getRoles().add(role);
    }

    private User getUserWithRoles(ResultSet resultSet) throws SQLException {
        List<User> users = new ArrayList<>();
        int row = 1;
        while (resultSet.next()) {
            User user = getUser(resultSet, row);
            if (users.contains(user)) {
                user = users.remove(users.size() - 1);
            }
            setRoles(user, resultSet);
            users.add(user);
            row++;
        }
        return users.get(0);
    }

    private List<User> getAllWithBooks(ResultSet resultSet) throws SQLException{
        List<User> users = new ArrayList<>();
        int row = 1;
        while (resultSet.next()) {
            User user = getUser(resultSet, row);
            if (users.contains(user)) {
                user = users.remove(users.size() - 1);
            }
            setBook(user, resultSet);
            users.add(user);
            row++;
        }
        return users;
    }
}
