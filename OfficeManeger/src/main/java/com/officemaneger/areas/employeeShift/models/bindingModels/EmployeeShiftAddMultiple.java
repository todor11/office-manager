package com.officemaneger.areas.employeeShift.models.bindingModels;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class EmployeeShiftAddMultiple {

    @NotNull(message = "Id на служителя не може да е празно поле.")
    private Long employeeId;

    @NotNull(message = "Id на смяната не може да е празно поле.")
    private Long shiftTypeId;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    private boolean addToHolidayToo;

    private boolean isAnnualLeave;

    private int numbOfAnnualLeave;

    public EmployeeShiftAddMultiple() {
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getShiftTypeId() {
        return this.shiftTypeId;
    }

    public void setShiftTypeId(Long shiftTypeId) {
        this.shiftTypeId = shiftTypeId;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public boolean getAddToHolidayToo() {
        return this.addToHolidayToo;
    }

    public void setAddToHolidayToo(boolean addToHolidayToo) {
        this.addToHolidayToo = addToHolidayToo;
    }

    public boolean getIsAnnualLeave() {
        return this.isAnnualLeave;
    }

    public void setIsAnnualLeave(boolean isAnnualLeave) {
        this.isAnnualLeave = isAnnualLeave;
    }

    public int getNumbOfAnnualLeave() {
        return this.numbOfAnnualLeave;
    }

    public void setNumbOfAnnualLeave(int numbOfAnnualLeave) {
        this.numbOfAnnualLeave = numbOfAnnualLeave;
    }
}
