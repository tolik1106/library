package com.zhitar.library.controller;

import com.zhitar.library.controller.action.Action;
import com.zhitar.library.controller.action.ActionFactory;
import com.zhitar.library.controller.interceptor.ControllerInterceptorService;
import com.zhitar.library.util.ExceptionUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.zhitar.library.util.PropertiesUtil.*;

public class MainController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(MainController.class.getName());

    {
        MY_APP_URL = getValue("app.url");
        REDIRECT_LENGTH = Integer.valueOf(getValue("app.redirect.length"));
        prefix = getValue("app.prefix");
        suffix = getValue("app.suffix");
    }

    private ControllerInterceptorService interceptorService = new ControllerInterceptorService();
    private String MY_APP_URL;
    private int REDIRECT_LENGTH;
    private String prefix;
    private String suffix;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        interceptorService.beforeAction(req, resp);

        String view = null;
        Action action = null;
        try {
            LOG.info("Service method " + req.getMethod() + " by path \"" + req.getServletPath() + "\" with query " + req.getQueryString());
            action = ActionFactory.getAction(req);
            view = action.execute(req, resp);
        } catch (Exception e) {
            view = interceptorService.afterException(req, resp, ExceptionUtil.getRootCause(e));
            if (view == null) {
                req.getSession().setAttribute("exceptionMessage", ExceptionUtil.getRootCause(e).getMessage());
                view = "redirect:error";
            }
        }
        if (!view.startsWith("redirect:")) {
            LOG.info("Do forward to jsp page " + view);
            req.getServletContext().getRequestDispatcher(prefix + view + suffix).forward(req, resp);
        } else {
            LOG.info("Do " + view);
            resp.sendRedirect(MY_APP_URL + view.substring(REDIRECT_LENGTH));
        }
    }

}