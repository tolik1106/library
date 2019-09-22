package com.zhitar.library.controller.action;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * Validate T entity
 * @param <T> entity class
 */
public interface RequestEntityChecker<T extends Serializable> {
    /**
     * Validate entity
     * @param entity entity class
     * @param request <code>HttpServletRequest</code>
     * @return <code>true</code> if entity is valid <code>false</code> otherwise
     */
    boolean check(T entity, HttpServletRequest request);
}
