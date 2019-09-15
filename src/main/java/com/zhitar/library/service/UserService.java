package com.zhitar.library.service;

import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.Role;
import com.zhitar.library.domain.User;
import com.zhitar.library.domain.UserRole;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.sql.TransactionHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class UserService {

    private static final Logger LOG = Logger.getLogger(UserService.class);

    private UserDao userDAO;
    private RoleDao roleDAO;
    private UserRoleDao userRoleDAO;

    public UserService(UserDao userDAO, RoleDao roleDAO, UserRoleDao userRoleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.userRoleDAO = userRoleDAO;
    }

    public User findByEmail(String email) {
        LOG.info("findByEmail: " + email);
        return TransactionHandler.readOnlyExecute(conn -> {
            User user = userDAO.findByEmail(email);
            if (user == null) {
                LOG.error("User with email '" + email + "' not found");
                throw new NotFoundException("message.emailnotfound.error");
            }
            return user;
        });
    }

    public User save(User user) {
        LOG.info("save user: " + user);
        return TransactionHandler.transactionExecute(conn -> {
            User saved = userDAO.save(user);
            LOG.debug("Saved user: " + saved);

            List<Role> roles = roleDAO.findAll();

            List<UserRole> userRoleList = new ArrayList<>();
            for (Role userRole : saved.getRoles()) {
                boolean newRole = true;
                for (Role role : roles) {
                    if (userRole.equals(role)) {
                        userRole.setId(role.getId());
                        newRole = false;
                    }
                }
                if (newRole) {
                    roleDAO.save(userRole);
                }
                userRoleList.add(new UserRole(userRole.getId(), saved.getId()));
            }
            userRoleDAO.save(userRoleList);
            return saved;
        });
    }
}
