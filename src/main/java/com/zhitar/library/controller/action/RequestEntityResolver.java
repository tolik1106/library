package com.zhitar.library.controller.action;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

public interface RequestEntityResolver<T extends Serializable> {
    T resolveEntity(HttpServletRequest request);
}
