package com.officemaneger.areas.phoneContact.models.bindingModels;

import javax.validation.constraints.NotNull;

public class RegisterPhoneContact {

    @NotNull(message = "Полето е задължително")
    private String phoneNumber;

    private String phoneType;

    private String description;

    public RegisterPhoneContact() {
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
