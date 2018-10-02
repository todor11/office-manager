package com.officemaneger.areas.annualLeave.models.viewModels;

public class AnnualLeaveWorkShiftViewModel {

    private Long id;

    private int numberOfAnnualLeave;

    private String annualLeaveType;

    private String annualLeaveTypeShort;

    private Long annualLeaveTypeId;

    public AnnualLeaveWorkShiftViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getNumberOfAnnualLeave() {
        return this.numberOfAnnualLeave;
    }

    public void setNumberOfAnnualLeave(int numberOfAnnualLeave) {
        this.numberOfAnnualLeave = numberOfAnnualLeave;
    }

    public String getAnnualLeaveType() {
        return this.annualLeaveType;
    }

    public void setAnnualLeaveType(String annualLeaveType) {
        this.annualLeaveType = annualLeaveType;
    }

    public String getAnnualLeaveTypeShort() {
        return this.annualLeaveTypeShort;
    }

    public void setAnnualLeaveTypeShort(String annualLeaveTypeShort) {
        this.annualLeaveTypeShort = annualLeaveTypeShort;
    }

    public Long getAnnualLeaveTypeId() {
        return this.annualLeaveTypeId;
    }

    public void setAnnualLeaveTypeId(Long annualLeaveTypeId) {
        this.annualLeaveTypeId = annualLeaveTypeId;
    }
}
