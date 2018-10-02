package com.officemaneger.areas.annualLeave.models.viewModels;

public class AnnualLeaveEmployeeViewHisCurrent {

    private String type;

    private int numbOfDays;

    public AnnualLeaveEmployeeViewHisCurrent() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumbOfDays() {
        return this.numbOfDays;
    }

    public void setNumbOfDays(int numbOfDays) {
        this.numbOfDays = numbOfDays;
    }
}
