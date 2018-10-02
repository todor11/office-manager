package com.officemaneger.areas.user.models.bindingModels;

import javax.validation.constraints.NotNull;

public class AdminUpdateIsEnabledModel {

    @NotNull(message = "error.accountOwnerId")
    private Long accountId;

    private boolean isEnabled;

    public AdminUpdateIsEnabledModel() {
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }
}
