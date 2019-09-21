package com.zhitar.library.util;

import com.zhitar.library.PropertiesHolder;

public class PropertiesUtil {
    private static final PropertiesHolder holder = new PropertiesHolder();

    static {
        holder.loadProperties("mysql");
        holder.loadProperties("app");
    }

    public static String getValue(String key) {
        return holder.getValue(key);
    }
}
