package com.officemaneger.areas.user.models.bindingModels;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class UserUpdateUsernameModel {

    @NotEmpty(message = "error.usernameEmpty")
    @Pattern(regexp="^[a-zA-Z0-9_]{5,30}$", message = "error.usernameShort")
    private String newUsername;

    private String password;

    @NotNull(message = "error.accountOwnerId")
    private Long accountId;

    public UserUpdateUsernameModel() {
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
}
