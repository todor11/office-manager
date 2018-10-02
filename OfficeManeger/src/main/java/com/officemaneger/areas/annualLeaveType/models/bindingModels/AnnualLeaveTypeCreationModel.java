package com.officemaneger.areas.annualLeaveType.models.bindingModels;

import org.hibernate.validator.constraints.NotEmpty;

public class AnnualLeaveTypeCreationModel {

    @NotEmpty(message = "error.title.notnull.fullName")
    private String fullName;

    @NotEmpty(message = "error.title.notnull.shortName")
    private String shortName;

    public AnnualLeaveTypeCreationModel() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }
}
