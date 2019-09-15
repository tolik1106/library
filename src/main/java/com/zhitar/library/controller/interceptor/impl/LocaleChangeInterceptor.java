package com.zhitar.library.controller.interceptor.impl;

import com.zhitar.library.controller.interceptor.BeforeActionInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public class LocaleChangeInterceptor implements BeforeActionInterceptor {

    @Override
    public void beforeAction(HttpServletRequest request, HttpServletResponse response) {
        String newLocale = request.getParameter("lang");
        if (newLocale != null) {
            request.getSession().setAttribute("lang", request.getParameter("lang"));
            response.setLocale(new Locale(request.getParameter("lang")));
        } else {
            String lang = (String) request.getSession().getAttribute("lang");
            if (lang != null) {
                response.setLocale(new Locale(lang));
            }
        }
    }
}
