package com.officemaneger.areas.annualLeaveDateInterval.models.viewModels;

public class AnnualLeaveDateIntervalViewModel {

    private String startDate;

    private String endDate;

    private String shiftTypeShortName;

    private int numbOfAnnualLeave;

    public AnnualLeaveDateIntervalViewModel() {
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

    public String getShiftTypeShortName() {
        return this.shiftTypeShortName;
    }

    public void setShiftTypeShortName(String shiftTypeShortName) {
        this.shiftTypeShortName = shiftTypeShortName;
    }

    public int getNumbOfAnnualLeave() {
        return this.numbOfAnnualLeave;
    }

    public void setNumbOfAnnualLeave(int numbOfAnnualLeave) {
        this.numbOfAnnualLeave = numbOfAnnualLeave;
    }
}
