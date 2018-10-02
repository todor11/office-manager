package com.officemaneger.areas.annualLeaveRequest.models.bindingModels;

import com.officemaneger.areas.annualLeaveDateInterval.models.bindingModels.AnnualLeaveDateIntervalUpdateModel;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class AnnualLeaveRequestUpdateModel {

    @NotNull(message = "error.annualLeaveDateInterval.idNull")
    private Long id;

    private Long employeeId;

    private List<AnnualLeaveDateIntervalUpdateModel> annualLeaveDateIntervals;

    public AnnualLeaveRequestUpdateModel() {
        this.setAnnualLeaveDateIntervals(new ArrayList<>());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<AnnualLeaveDateIntervalUpdateModel> getAnnualLeaveDateIntervals() {
        return this.annualLeaveDateIntervals;
    }

    public void setAnnualLeaveDateIntervals(List<AnnualLeaveDateIntervalUpdateModel> annualLeaveDateIntervals) {
        this.annualLeaveDateIntervals = annualLeaveDateIntervals;
    }
}
