package com.zhitar.library.util;

import com.zhitar.library.annotation.Table;
import org.apache.log4j.Logger;

public class TableNameResolver {

    private static final Logger LOG = Logger.getLogger(TableNameResolver.class.getName());

    public static String getTableName(Class<?> clazz) {
        LOG.info("Execute getTableName for " + clazz.getName());
        String result = null;
        final Table annotation = clazz.getAnnotation(Table.class);
        if (annotation != null) {
            result = annotation.value();
            LOG.trace("Found annotation with value " + result);
        } else {
            LOG.trace("Annotation not found. Resolving table name by classname");
            result = clazz.getSimpleName();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < result.length(); i++) {
                char currentChar = result.charAt(i);
                if (Character.isUpperCase(currentChar) && i != 0) {
                    builder.append("_");
                }
                builder.append(currentChar);
            }
            result = builder.toString().toLowerCase();
        }
        LOG.trace("Resolved table name for class " + clazz.getName() + " is " + result);
        return result;
    }
}
