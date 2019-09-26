package com.zhitar.library.service.impl;

import com.zhitar.library.annotation.Connectivity;
import com.zhitar.library.annotation.Transaction;
import com.zhitar.library.dao.auxiliarydao.UserRoleDao;
import com.zhitar.library.dao.roledao.RoleDao;
import com.zhitar.library.dao.userdao.UserDao;
import com.zhitar.library.domain.Role;
import com.zhitar.library.domain.User;
import com.zhitar.library.domain.UserRole;
import com.zhitar.library.exception.NotFoundException;
import com.zhitar.library.service.UserService;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

@Connectivity
public class UserServiceImpl implements UserService {

    private static final Logger LOG = Logger.getLogger(UserServiceImpl.class);

    private UserDao userDAO;
    private RoleDao roleDAO;
    private UserRoleDao userRoleDAO;

    public UserServiceImpl(UserDao userDAO, RoleDao roleDAO, UserRoleDao userRoleDAO) {
        this.userDAO = userDAO;
        this.roleDAO = roleDAO;
        this.userRoleDAO = userRoleDAO;
    }

    @Transaction(readOnly = true)
    @Override
    public User findByEmail(String email) {
        LOG.info("findByEmail: " + email);
            User user = userDAO.findByEmail(email);
            if (user == null) {
                LOG.error("User with email '" + email + "' not found");
                throw new NotFoundException("message.emailnotfound.error");
            }
            return user;
    }

    @Transaction
    @Override
    public User save(User user) {
        LOG.info("save user: " + user);
            User saved = userDAO.save(user);
            LOG.debug("Saved user: " + saved);

            List<Role> roles = roleDAO.findAll();

            List<UserRole> userRoleList = new ArrayList<>();
            for (Role userRole : saved.getRoles()) {
                for (Role role : roles) {
                    if (userRole.equals(role)) {
                        userRole.setId(role.getId());
                    }
                }
                userRoleList.add(new UserRole(userRole.getId(), saved.getId()));
            }
            userRoleDAO.save(userRoleList);
            return saved;
    }
}
