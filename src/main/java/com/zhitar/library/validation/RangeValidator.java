package com.zhitar.library.validation;

import com.zhitar.library.validation.annotation.Range;

import java.lang.reflect.Field;

import static com.zhitar.library.validation.ValidationService.SUFFIX;

public class RangeValidator implements Validator {
    @Override
    public <T> Error validate(Field field, T obj) {
        String prefix = obj.getClass().getSimpleName().toLowerCase();

        Range annotation = field.getAnnotation(Range.class);
        int min = annotation.min();
        int max = annotation.max();
        field.setAccessible(true);
        try {
            Object value = field.get(obj);
            if (value != null) {
                if (value instanceof Number) {
                    Number num = (Number) value;
                    if (num.intValue() >= min && num.intValue() <= max) {
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
