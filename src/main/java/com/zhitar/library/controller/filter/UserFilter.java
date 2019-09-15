package com.zhitar.library.controller.filter;

import com.zhitar.library.domain.User;
import com.zhitar.library.util.PropertiesUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class UserFilter implements Redirect {

    private static final Logger LOG = Logger.getLogger(UserFilter.class.getName());


    private static final Set<String> userDenied = new HashSet<>();
    private static final Set<String> authorizedDenied = new HashSet<>();
    static {
        userDenied.add("/admin/**");

        authorizedDenied.add("/login");
        authorizedDenied.add("/register");
        LOG.trace("Add denied access for 'USER' " + userDenied + " and denied access to " + authorizedDenied);
    }

    public void doFilter(User user, String servletPath, HttpServletResponse response, HttpServletRequest request, FilterChain chain) throws IOException, ServletException {
        LOG.trace("do filter for path " + servletPath);
        if (!authorizedDenied.contains(servletPath)) {
            if (user.hasRole("ADMIN")) {
                LOG.trace("User with role 'ADMIN' got access");
                chain.doFilter(request, response);
            } else {
                if (!servletPath.matches("(/admin/)((\\w+)(/)?)*")) {
                    LOG.trace("User with another role got access");
                    chain.doFilter(request, response);
                } else {
                    LOG.trace("Redirect to /books");
                    sendRedirect(response);
                }
            }
        } else {
            sendRedirect(response);
        }
    }

    public void sendRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(PropertiesUtil.getValue("app.url") + PropertiesUtil.getValue("app.showbooks.action").substring(1));
    }
}
