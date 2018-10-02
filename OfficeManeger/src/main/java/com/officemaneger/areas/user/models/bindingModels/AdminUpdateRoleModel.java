package com.officemaneger.areas.user.models.bindingModels;

import javax.validation.constraints.NotNull;

public class AdminUpdateRoleModel {

    @NotNull(message = "error.accountOwnerId")
    private Long accountId;

    @NotNull(message = "error.accountRoleId")
    private Long roleId;

    public AdminUpdateRoleModel() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }
}
