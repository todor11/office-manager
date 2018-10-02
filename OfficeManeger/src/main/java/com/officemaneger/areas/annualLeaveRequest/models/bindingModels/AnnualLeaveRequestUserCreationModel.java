package com.officemaneger.areas.annualLeaveRequest.models.bindingModels;

import com.officemaneger.areas.annualLeaveDateInterval.models.bindingModels.AnnualLeaveDateIntervalCreationModel;

import java.util.ArrayList;
import java.util.List;

public class AnnualLeaveRequestUserCreationModel {

    private Long employeeId;

    private List<AnnualLeaveDateIntervalCreationModel> annualLeaveDateIntervals;

    public AnnualLeaveRequestUserCreationModel() {
        this.setAnnualLeaveDateIntervals(new ArrayList<>());
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public List<AnnualLeaveDateIntervalCreationModel> getAnnualLeaveDateIntervals() {
        return this.annualLeaveDateIntervals;
    }

    public void setAnnualLeaveDateIntervals(List<AnnualLeaveDateIntervalCreationModel> annualLeaveDateIntervals) {
        this.annualLeaveDateIntervals = annualLeaveDateIntervals;
    }
}
