package com.officemaneger.areas.role.models.viewModels;

public class RoleViewModel {

    private long id;

    private String authority;

    public RoleViewModel() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAuthority() {
        return this.authority;
    }

    public void setAuthority(String authority) {
        this.authority = authority;
    }
}
