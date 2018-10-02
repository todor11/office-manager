package com.officemaneger.areas.phoneContact.models.bindingModels;

import javax.validation.constraints.NotNull;

public class PhoneContactUpdateModel {

    private Long id;

    @NotNull(message = "Полето е задължително")
    private String phoneNumber;

    private String phoneType;

    private String description;

    public PhoneContactUpdateModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
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
