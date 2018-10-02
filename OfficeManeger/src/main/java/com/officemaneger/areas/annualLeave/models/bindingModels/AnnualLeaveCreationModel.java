package com.officemaneger.areas.annualLeave.models.bindingModels;

import javax.validation.constraints.NotNull;

public class AnnualLeaveCreationModel {

    @NotNull(message = "Не може типа на отпуската да е празно поле")
    private Long annualLeaveTypeId;

    @NotNull(message = "Не може броя на дните да е празно поле")
    private Integer numberOfAnnualLeave;

    @NotNull(message = "Не може служителя да е празно поле")
    private Long employeeId;

    public AnnualLeaveCreationModel() {
    }

    public Long getAnnualLeaveTypeId() {
        return this.annualLeaveTypeId;
    }

    public void setAnnualLeaveTypeId(Long annualLeaveTypeId) {
        this.annualLeaveTypeId = annualLeaveTypeId;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Integer getNumberOfAnnualLeave() {
        return numberOfAnnualLeave;
    }

    public void setNumberOfAnnualLeave(Integer numberOfAnnualLeave) {
        this.numberOfAnnualLeave = numberOfAnnualLeave;
    }
}
