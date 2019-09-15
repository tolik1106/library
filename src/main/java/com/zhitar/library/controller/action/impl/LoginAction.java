package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.AbstractAction;
import com.zhitar.library.domain.User;
import com.zhitar.library.service.UserService;
import com.zhitar.library.util.PropertiesUtil;
import com.zhitar.library.validation.ValidationResult;
import com.zhitar.library.validation.ValidationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginAction extends AbstractAction {

    private static final Logger LOG = Logger.getLogger(LoginAction.class);

    private UserService userService = Container.getInstance().getBean(UserService.class);
    private ValidationService validator = Container.getInstance().getBean(ValidationService.class);

    @Override
    protected String doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("doGet method");
        request.setAttribute("register", false);
        return PropertiesUtil.getValue("app.login.page");
    }

    @Override
    protected String doPost(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        LOG.info("doPost with email: " + email + " and phone: " + phone);
        User user = new User(null, "unknown", email, phone);
        request.setAttribute("registeredUser", user);
        LOG.debug("validating user");
        ValidationResult validationResult = validator.validate(User.class, user);
        if (validationResult.hasErrors()) {
            LOG.debug("User has errors");
            while (validationResult.hasErrors()) {
                request.setAttribute(validationResult.getField(), validationResult.getMessage());
                validationResult.next();
            }
            return "login";
        }
        LOG.debug("User validated successful");
        user = userService.findByEmail(email);
        if (!phone.equals(user.getPhone())) {
            LOG.debug("User phone doesn't match: " + phone + " - " + user.getPhone());
            request.setAttribute("userphoneError", "Phone isn't correct. Please try again");
            return "login";
        } else {
            LOG.debug("add user to session: " + user);
            request.getSession().setAttribute("user", user);
            return "redirect:books";
        }
    }
}
