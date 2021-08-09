package com.playtomic.tests.wallet.utils;

import javax.validation.Validation;
import javax.validation.Validator;

public class ValidationUtils<T> {

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public boolean isValid(T input){
        return validator.validate(input).isEmpty();
    }
}
