package com.officemaneger.areas.annualLeaveDateInterval.models.bindingModels;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class AnnualLeaveDateIntervalUpdateModel {

    private Long id;

    @NotNull(message = "error.annualLeaveDateInterval.annualLeaveTypeIdNull")
    private Long annualLeaveTypeId;

    @NotNull(message = "error.annualLeaveDateInterval.startDateNull")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @NotNull(message = "error.annualLeaveDateInterval.endDateNull")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    public AnnualLeaveDateIntervalUpdateModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAnnualLeaveTypeId() {
        return this.annualLeaveTypeId;
    }

    public void setAnnualLeaveTypeId(Long annualLeaveTypeId) {
        this.annualLeaveTypeId = annualLeaveTypeId;
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
}
