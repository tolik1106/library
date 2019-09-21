package com.zhitar.library.controller.interceptor;

import com.zhitar.library.controller.interceptor.impl.DaoExceptionInterceptor;
import com.zhitar.library.controller.interceptor.impl.LocaleChangeInterceptor;
import com.zhitar.library.controller.interceptor.impl.NotFoundExceptionInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class ControllerInterceptorService implements BeforeActionInterceptor, ExceptionInterceptor {

    private List<BeforeActionInterceptor> beforeInterceptors = new ArrayList<>();
    private List<ControllerExceptionInterceptor> exceptionInterceptors = new ArrayList<>();

    {
        beforeInterceptors.add(new LocaleChangeInterceptor());
        exceptionInterceptors.add(new NotFoundExceptionInterceptor());
        exceptionInterceptors.add(new DaoExceptionInterceptor());
    }
    @Override
    public void beforeAction(HttpServletRequest request, HttpServletResponse response) {
        for (BeforeActionInterceptor interceptor : beforeInterceptors) {
            interceptor.beforeAction(request, response);
        }
    }

    @Override
    public String afterException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        for (ControllerExceptionInterceptor exceptionInterceptor : exceptionInterceptors) {
            if (exceptionInterceptor.canHandle(e)) {
                return exceptionInterceptor.afterException(request, response, e);
            }
        }
        return null;
    }

    public boolean addBeforeActionInterceptor(BeforeActionInterceptor interceptor) {
        return beforeInterceptors.add(interceptor);
    }

    public void removeBeforeActionInterceptor(BeforeActionInterceptor interceptor) {
        beforeInterceptors.remove(interceptor);
    }

    public boolean addExceptionInterceptor(ControllerExceptionInterceptor interceptor) {
        return exceptionInterceptors.add(interceptor);
    }

    public void removeExceptionInterceptor(ControllerExceptionInterceptor interceptor) {
        exceptionInterceptors.remove(interceptor);
    }
}
