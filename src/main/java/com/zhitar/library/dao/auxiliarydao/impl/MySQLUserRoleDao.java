package com.zhitar.library.dao.auxiliarydao.impl;

import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.domain.UserRole;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.DaoHelper;
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
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, USER_ID, ROLE_ID).build();

    public MySQLUserRoleDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public UserRole save(UserRole userRole) {
        return DaoHelper.getInstance().save(INSERT_QUERY, userRole, userRole.getUserId(), userRole.getRoleId());
    }

    @Override
    public List<UserRole> save(List<UserRole> userRoleList) {
        LOG.info("Execute save with userRoleList " + userRoleList);
        if (userRoleList.size() == 1) {
            return Collections.singletonList(save(userRoleList.get(0)));
        }
        Object[] params = new Object[userRoleList.size() * 2];
        int index = 0;
        for (UserRole userRole : userRoleList) {
            params[index++] = userRole.getUserId();
            params[index++] = userRole.getRoleId();
        }
        return DaoHelper.getInstance().saveBatch(
                INSERT_QUERY,
                userRoleList,
                params
        );
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
        return DaoHelper.getInstance().findList(
                new QueryBuilder().select()
                        .table(TABLE)
                        .whereAssign(column)
                        .build(),
                this::getUserRole,
                value
        );
    }

    private UserRole getUserRole(ResultSet resultSet, int row) throws SQLException {
        UserRole userRole = new UserRole();
        userRole.setRoleId(resultSet.getInt(1));
        userRole.setUserId(resultSet.getInt(2));
        LOG.trace("Retrieved userRole " + userRole);

        return userRole;
    }
}
