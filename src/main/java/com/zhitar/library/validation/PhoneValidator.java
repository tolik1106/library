package com.zhitar.library.validation;

import com.zhitar.library.validation.annotation.Phone;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhitar.library.validation.ValidationService.SUFFIX;

public class PhoneValidator implements Validator {
    @Override
    public <T> Error validate(Field field, T obj) {
        String prefix = obj.getClass().getSimpleName().toLowerCase();
        Phone annotation = field.getAnnotation(Phone.class);
        field.setAccessible(true);

        Error error = null;
        try {
            Object value = field.get(obj);
            if (value == null) {
                error = new Error(field.getName(), annotation.message());
            } else if (value instanceof String) {
                String phone = (String) value;
                String phonePattern = annotation.pattern();
                Pattern pattern = Pattern.compile(phonePattern);
                Matcher matcher = pattern.matcher(phone);
                if (!matcher.matches()) {
                    error = new Error(prefix + field.getName() + SUFFIX, annotation.message());
                }
            }
            return error;
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
