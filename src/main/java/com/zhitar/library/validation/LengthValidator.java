package com.zhitar.library.validation;

import com.zhitar.library.validation.annotation.Length;

import java.lang.reflect.Field;

import static com.zhitar.library.validation.ValidationService.SUFFIX;

public class LengthValidator implements Validator {

    public <T> Error validate(Field field, T obj) {
        String prefix = obj.getClass().getSimpleName().toLowerCase();
        Length annotation = field.getAnnotation(Length.class);
        int min = annotation.min();
        int max = annotation.max();
        field.setAccessible(true);
        try {
            Object value = field.get(obj);
            if (value != null) {
                if (value instanceof String) {
                    String string = (String) value;
                    if (!string.trim().isEmpty() && (string.length() >= min && string.length() <= max)) {
                        return null;
                    }
                }
            }
            return new Error(prefix + field.getName() + SUFFIX, annotation.message());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
