package com.zhitar.library.dao.auxiliarydao.impl;

import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.domain.UserRole;
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

public class MySQLUserRoleDao implements UserRoleDao {

    private static final Logger LOG = Logger.getLogger(MySQLUserRoleDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(UserRole.class);
    private static final String USER_ID = "user_id";
    private static final String ROLE_ID = "role_id";

    public MySQLUserRoleDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public UserRole save(UserRole userRole) {
        LOG.info("Execute save with userRole " + userRole);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, USER_ID, ROLE_ID).build()
        )) {
            statement.setInt(1, userRole.getUserId());
            statement.setInt(2, userRole.getRoleId());
            LOG.trace("executeUpdate statement");
            statement.executeUpdate();
            return userRole;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserRole> save(List<UserRole> userRoleList) {
        LOG.info("Execute save with userRoleList " + userRoleList);
        if (userRoleList.size() == 1) {
            return Collections.singletonList(save(userRoleList.get(0)));
        }
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().insert(TABLE, USER_ID, ROLE_ID).build()
        )) {
            LOG.trace("Prepare batch");
            for (UserRole userRole : userRoleList) {
                statement.setInt(1, userRole.getUserId());
                statement.setInt(2, userRole.getRoleId());
                statement.addBatch();
            }
            LOG.trace("executeBatch statement");
            statement.executeBatch();
            return userRoleList;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    @Override
    public List<UserRole> findByRoleId(Integer roleId) {
        return findById(ROLE_ID, roleId);
    }

    @Override
    public List<UserRole> findByUserId(Integer userId) {
        return findById(USER_ID, userId);
    }

    private List<UserRole> findById(String column, Integer value) {
        LOG.info("Execute findById with column " + column + " and id " + value);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                        .table(TABLE)
                        .whereAssign(column)
                        .build()
        )) {
            statement.setInt(1, value);
            LOG.trace("executeQuery statement");
            ResultSet resultSet = statement.executeQuery();
            return getUserRoles(resultSet);
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage(), e);
        }
    }

    private List<UserRole> getUserRoles(ResultSet resultSet) throws SQLException {
        List<UserRole> userRoleList = new ArrayList<>();
        while (resultSet.next()) {
            userRoleList.add(getUserRole(resultSet));
        }
        LOG.trace("Retrieved userRoles " + userRoleList);
        return userRoleList;
    }

    private UserRole getUserRole(ResultSet resultSet) throws SQLException {
        UserRole userRole = new UserRole();
        userRole.setRoleId(resultSet.getInt(1));
        userRole.setUserId(resultSet.getInt(2));
        LOG.trace("Retrieved userRole " + userRole);

        return userRole;
    }
}
