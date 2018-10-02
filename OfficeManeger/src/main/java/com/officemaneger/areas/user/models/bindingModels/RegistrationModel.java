package com.officemaneger.areas.user.models.bindingModels;


import com.officemaneger.annotations.IsPasswordsMatching;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@IsPasswordsMatching
public class RegistrationModel {

    @Size(min = 5, message = "Името трябва да е повече от 5 символа !")
    private String username;

    @Size(min = 5, message = "Паролата трябва да е поне 5 символа !")
    private String password;

    private String confirmPassword;

    @NotNull(message = "Служителя не може да е празно поле!")
    private Long employeeId;

    @NotNull(message = "Ролята не може да е празно поле!")
    private Long roleId;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
