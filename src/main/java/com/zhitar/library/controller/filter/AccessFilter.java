package com.zhitar.library.controller.filter;

import com.zhitar.library.domain.User;
import com.zhitar.library.util.PropertiesUtil;
import org.apache.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Filter HttpServletRequest for accessing.
 * If HttpSession doesn't have user then
 * all paths except /login, /register and / will redirect to login page
 * Otherwise pass checks to {@link UserFilter#doFilter(User, String, HttpServletResponse, HttpServletRequest, FilterChain)}
 */
public class AccessFilter implements Filter, Redirect {

    private static final Logger LOG = Logger.getLogger(AccessFilter.class.getName());

    private static final Set<String> allPermission = new HashSet<>();
    private static final Set<String> authPermission = new HashSet<>();

    static {
        allPermission.add("/");
        allPermission.add("(/img/)(.)*");
        allPermission.add("(/webjars/)(.)*");

        authPermission.add("/login");
        authPermission.add("/register");
        LOG.trace("Add access for all " + allPermission + " and for auth " + authPermission);
    }

    private UserFilter userFilter  = new UserFilter();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String servletPath = httpRequest.getServletPath();
        LOG.info("filter request " + servletPath);
        //All permission
        if (allPermission.contains(servletPath) || allPermission.stream().anyMatch(servletPath::matches)) {
            LOG.debug("FilterChain.doFilter()");
            chain.doFilter(request, response);
        } else {
            HttpSession session = httpRequest.getSession();
            User user = (User) session.getAttribute("user");
            if (user != null) {
                LOG.debug("userFilter access for user " + user);
                userFilter.doFilter(user, servletPath, httpResponse, httpRequest, chain);
            } else {
                if (authPermission.contains(servletPath)) {
                    chain.doFilter(request, response);
                } else {
                    LOG.trace("Redirect to login page");
                    sendRedirect(httpResponse);
                }
            }
        }
    }

    @Override
    public void sendRedirect(HttpServletResponse response) throws IOException {
        response.sendRedirect(PropertiesUtil.getValue("app.url") + PropertiesUtil.getValue("app.login.page"));
    }
}