package com.zhitar.library.controller.action;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface RequestEntityChecker<T extends Serializable> {
    boolean check(T entity, HttpServletRequest request);
}
