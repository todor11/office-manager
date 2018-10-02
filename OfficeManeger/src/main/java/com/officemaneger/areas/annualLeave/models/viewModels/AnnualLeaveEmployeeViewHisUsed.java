package com.officemaneger.areas.annualLeave.models.viewModels;

public class AnnualLeaveEmployeeViewHisUsed {

    private String type;

    private String startDate;

    private String endDate;

    private int numbOfDays;

    public AnnualLeaveEmployeeViewHisUsed() {
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return this.endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public int getNumbOfDays() {
        return this.numbOfDays;
    }

    public void setNumbOfDays(int numbOfDays) {
        this.numbOfDays = numbOfDays;
    }
}
