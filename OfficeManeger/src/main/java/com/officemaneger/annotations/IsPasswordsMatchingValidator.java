package com.officemaneger.annotations;

import com.officemaneger.areas.user.models.bindingModels.RegistrationModel;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IsPasswordsMatchingValidator implements ConstraintValidator<IsPasswordsMatching, Object> {
    @Override
    public void initialize(IsPasswordsMatching isPasswordsMatching) {

    }

    @Override
    public boolean isValid(Object userClass, ConstraintValidatorContext constraintValidatorContext) {
        if(userClass instanceof RegistrationModel){
            return ((RegistrationModel) userClass).getPassword().equals(((RegistrationModel) userClass).getConfirmPassword());
        } //else if(userClass instanceof UserPasswordChangeModel){
            //return ((UserPasswordChangeModel) userClass).getPassword().equals(((UserPasswordChangeModel) userClass).getConfirmPassword());
        //}

        return false;
    }
}
