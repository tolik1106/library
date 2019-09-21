package com.zhitar.library.dao.roledao.impl;

import com.zhitar.library.dao.AbstractDao;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.domain.Role;
import com.zhitar.library.exception.DaoException;
import com.zhitar.library.sql.QueryBuilder;
import com.zhitar.library.sql.TransactionHandler;
import com.zhitar.library.util.TableNameResolver;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MySQLRoleDao extends AbstractDao<Role, Integer> implements RoleDao {

    private static final Logger LOG = Logger.getLogger(MySQLRoleDao.class.getName());

    private static final String TABLE = TableNameResolver.getTableName(Role.class);
    private static final String ID_COLUMN = "id";
    private static final String ROLE_COLUMN = "role";

    public MySQLRoleDao() {
        LOG.trace("Instantiating " + this.getClass().getName());
    }

    @Override
    public Role save(Role entity) {
        LOG.info("Execute save with role " + entity);
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder()
                        .insert(TABLE, ROLE_COLUMN)
                        .build(),
                Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, entity.getRole());
            LOG.trace("executeUpdate statement");
            statement.executeUpdate();
            final ResultSet generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int key = generatedKeys.getInt(1);
                LOG.trace("Generated key is " + key);
                entity.setId(key);
            }
            LOG.trace("Saved role " + entity);
            return entity;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Role update(Role entity) {
        LOG.info("Execute update with role " + entity);
        return super.update(entity);
    }

    @Override
    public Role findById(Integer id) {
        LOG.info("Execute findById with id " + id);
        Role role = null;
        try (PreparedStatement statement = TransactionHandler.getConnection().prepareStatement(
                new QueryBuilder().select()
                        .table(TABLE)
                        .whereAssign(ID_COLUMN)
                        .build())
        ) {
            statement.setInt(1, id);
            LOG.trace("executeQuery statement");

            ResultSet selectSet = statement.executeQuery();
            if (selectSet.next()) {
                role = getRole(selectSet);
                LOG.trace("Role " + role);
            }
            return role;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Role> findAll() {
        LOG.info("Execute findAll");
        try (Statement statement = TransactionHandler.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(
                    new QueryBuilder().select()
                    .table(TABLE)
                    .build()
            );
            LOG.trace("executeQuery statement");

            List<Role> roles = new ArrayList<>();
            while (resultSet.next()) {
                Role role = getRole(resultSet);
                LOG.trace("Role " + role);
                roles.add(role);
            }
            LOG.trace("Role List " + roles);
            return roles;
        } catch (SQLException e) {
            LOG.error("An error occurred during execution", e);
            throw new DaoException(e.getMessage());
        }
    }

    private Role getRole(ResultSet resultSet) throws SQLException {
        Role role = new Role();
        role.setId(resultSet.getInt(1));
        role.setRole(resultSet.getString(2));
        return role;
    }

    @Override
    protected void update(Role entity, Connection connection) throws SQLException {
        try (PreparedStatement updateStatement = connection.prepareStatement(
                new QueryBuilder().update(TABLE, ROLE_COLUMN)
                        .whereAssign(ID_COLUMN)
                        .build()
        )) {
            updateStatement.setString(1, entity.getRole());
            updateStatement.setInt(2, entity.getId());
            updateStatement.executeUpdate();
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
