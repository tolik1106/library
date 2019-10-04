package com.zhitar.library.dao.roledao.impl;

import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.domain.Role;
import com.zhitar.library.sql.DaoHelper;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class MySQLRoleDao implements RoleDao {

    private static final Logger LOG = Logger.getLogger(MySQLRoleDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Role.class);
    private static final String ID_COLUMN = "id";
    private static final String ROLE_COLUMN = "role";
    private static final String UPDATE_QUERY = new QueryBuilder().update(TABLE, ROLE_COLUMN)
            .whereAssign(ID_COLUMN)
            .build();
    private static final String FIND_ALL_QUERY = new QueryBuilder().select().table(TABLE).build();
    private static final String FIND_BY_ID_QUERY = new QueryBuilder().select().table(TABLE).whereAssign(ID_COLUMN).build();
    private static final String INSERT_QUERY = new QueryBuilder().insert(TABLE, ROLE_COLUMN).build();
    private static final String DELETE_QUERY = new QueryBuilder().delete(TABLE).whereAssign(ID_COLUMN).build();

    public MySQLRoleDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public Role save(Role entity) {
        Object[] params = {entity.getRole()};
        return DaoHelper.getInstance().save(INSERT_QUERY, entity, Integer.class, params);
    }

    @Override
    public Role update(Role entity) {
        Object[] params = {entity.getRole(), entity.getId()};
        return DaoHelper.getInstance().executeUpdate(UPDATE_QUERY, entity, params);
    }

    @Override
    public Role findById(Integer id) {
        return DaoHelper.getInstance().find(FIND_BY_ID_QUERY, this::getRole, id);
    }

    @Override
    public List<Role> findAll() {
        return DaoHelper.getInstance().findAll(FIND_ALL_QUERY, this::getRole);
    }

    @Override
    public boolean delete(Integer id) {
        return DaoHelper.getInstance().delete(DELETE_QUERY, id);
    }

    private Role getRole(ResultSet resultSet, int row) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getInt(1));
        role.setRole(resultSet.getString(2));
        return role;
    }
}