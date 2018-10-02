package com.officemaneger.areas.annualLeaveType.models.viewModels;

public class AnnualLeaveTypeViewModel {

    private Long id;

    private String fullName;

    private String shortName;

    public AnnualLeaveTypeViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
