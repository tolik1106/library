package com.zhitar.library.controller.filter;

import javax.servlet.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Simple implementation of {@link Filter} interface
 * It is change request and response encoding with {@link #encoding}
 */
public class CharacterEncodingFilter implements Filter {

    /**
     * String representation of encoding {@link StandardCharsets}
     */
    private String encoding;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        encoding = filterConfig.getInitParameter("encoding");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding(encoding);
        response.setCharacterEncoding(encoding);
        chain.doFilter(request, response);
    }
}
