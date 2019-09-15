package com.zhitar.library.validation;

import java.lang.reflect.Field;

public interface Validator {
    <T> Error validate(Field field, T obj);
}
