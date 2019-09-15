package com.zhitar.library.controller.action.impl;

import com.zhitar.library.Container;
import com.zhitar.library.controller.action.AbstractAction;
import com.zhitar.library.domain.User;
import com.zhitar.library.service.UserService;
import com.zhitar.library.validation.ValidationResult;
import com.zhitar.library.validation.ValidationService;
import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RegisterAction extends AbstractAction {

    private static final Logger LOG = Logger.getLogger(ReadersAction.class);

    private UserService userService = Container.getInstance().getBean(UserService.class);
    private ValidationService validator = Container.getInstance().getBean(ValidationService.class);

    @Override
    protected String doGet(HttpServletRequest request, HttpServletResponse response) {
        LOG.info("doGet method");
        request.setAttribute("register", true);
        return "login";
    }

    @Override
    protected String doPost(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        LOG.info("doPost with name: " + name + ", email: " + email + ", phone: " + phone);
        User user = new User(null, name, email, phone);
        request.setAttribute("registeredUser", user);
        LOG.debug("validating user " + user);
        ValidationResult validationResult = validator.validate(User.class, user);

        if (validationResult.hasErrors()) {
            LOG.debug("User has errors");
            while (validationResult.hasErrors()) {
                request.setAttribute(validationResult.getField(), validationResult.getMessage());
                validationResult.next();
            }
            request.setAttribute("register", true);
            return "login";
        }
        LOG.debug("User validated. Save user");
        userService.save(user);
        return "redirect:login";
    }
}
