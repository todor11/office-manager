package com.officemaneger.configs.errors;

import org.springframework.validation.FieldError;

public class CustomFieldError extends Error {

    private FieldError fieldError;

    public CustomFieldError(String message, FieldError fieldError) {
        super(message);
        this.fieldError = fieldError;
    }

    public FieldError getFieldError() {
        return fieldError;
    }

    public void setFieldError(FieldError fieldError) {
        this.fieldError = fieldError;
    }
}
