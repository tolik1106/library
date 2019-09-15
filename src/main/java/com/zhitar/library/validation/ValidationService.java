package com.zhitar.library.validation;

import com.zhitar.library.validation.annotation.Email;
import com.zhitar.library.validation.annotation.Length;
import com.zhitar.library.validation.annotation.Phone;
import com.zhitar.library.validation.annotation.Range;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.HashMap;

public class ValidationService {

    static final String SUFFIX = "Error";

    private HashMap<Class<? extends Annotation>, Validator> validatorsMap = new HashMap<>();

    {
        validatorsMap.put(Length.class, new LengthValidator());
        validatorsMap.put(Email.class, new EmailValidator());
        validatorsMap.put(Phone.class, new PhoneValidator());
        validatorsMap.put(Range.class, new RangeValidator());
    }

    public <T> ValidationResult validate(Class<? super T> clazz, T obj) {
        ValidationResult validationResult = new ValidationResult();

        while (clazz != Object.class) {
            final Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    Class<? extends Annotation> annotationType = annotation.annotationType();
                    if (validatorsMap.containsKey(annotationType)) {
                        Validator validator = validatorsMap.get(annotationType);
                        Error result = validator.validate(field, obj);
                        if (result != null) {
                            validationResult.add(result);
                        }
                    }
                }
            }
            clazz = clazz.getSuperclass();
        }
        return validationResult;
    }
}
