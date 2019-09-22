package com.zhitar.library.controller.action;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Implementation of this interface map request parameters
 * in T object
 * @param <T> entity class
 */
public interface RequestEntityResolver<T extends Serializable> {
    T resolveEntity(HttpServletRequest request);
}
