package com.officemaneger.areas.user.models.viewModels;

import com.officemaneger.areas.role.models.viewModels.RoleViewModel;

public class AccountViewModel {

    private Long id;

    private String username;

    private boolean isEnabled;

    private RoleViewModel role;

    public AccountViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean getIsEnabled() {
        return this.isEnabled;
    }

    public void setIsEnabled(boolean isEnabled) {
        this.isEnabled = isEnabled;
    }

    public RoleViewModel getRole() {
        return this.role;
    }

    public void setRole(RoleViewModel role) {
        this.role = role;
    }
}
