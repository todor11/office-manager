package com.officemaneger.areas.user.models.bindingModels;

import com.officemaneger.annotations.IsPasswordsMatching;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

//@IsPasswordsMatching
public class UserPasswordChangeModel {

    @NotEmpty(message = "error.passwordEmpty")
    @Pattern(regexp="^.{5,30}$", message = "error.passwordShort")
    private String oldPassword;

    @NotEmpty(message = "error.passwordEmpty")
    @Pattern(regexp="^.{5,30}$", message = "error.passwordShort")
    private String password;

    @NotEmpty(message = "error.passwordEmpty")
    @Pattern(regexp="^.{5,30}$", message = "error.passwordShort")
    private String confirmPassword;

    public UserPasswordChangeModel() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}
