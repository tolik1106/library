package com.zhitar.library.validation;

import com.zhitar.library.validation.annotation.Email;

import java.lang.reflect.Field;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.zhitar.library.validation.ValidationService.SUFFIX;

public class EmailValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-+]+" +
            "(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*" +
            "(\\.[A-Za-z]{2,})$";

    @Override
    public <T> Error validate(Field field, T obj) {
        String prefix = obj.getClass().getSimpleName().toLowerCase();
        Email annotation = field.getAnnotation(Email.class);
        field.setAccessible(true);
        try {
            final Object value = field.get(obj);
            if (value != null) {
                if (value instanceof String) {
                    String email = (String) value;
                    Pattern pattern = Pattern.compile(EMAIL_PATTERN);
                    Matcher matcher = pattern.matcher(email);
                    if (matcher.matches()) {
                        return null;
                    }
                }
            }
            return new Error(prefix + field.getName() + SUFFIX, annotation.message());
        } catch (IllegalAccessException e) {
            throw new RuntimeException();
        }
    }
}
