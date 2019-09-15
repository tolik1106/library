package com.zhitar.library.validation;

import java.util.*;

public class ValidationResult {

    private List<Error> errors = new ArrayList<>();
    private int index = 0;

    public boolean add(Error error) {
        return errors.add(error);
    }

    public String getField() {
        return errors.get(index).getFieldName();
    }

    public String getMessage() {
        return errors.get(index).getErrorMessage();
    }

    public void reset() {
        index = 0;
    }

    public void next() {
        index++;
    }

    public boolean hasErrors() {
        return index < errors.size();
    }
}