package com.zhitar.library.controller.filter;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface Redirect {
    void sendRedirect(HttpServletResponse response) throws IOException;
}
