package com.officemaneger.areas.employee.models.viewModels;

import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveWorkShiftViewModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeeWorkShiftViewModel {

    private Long id;

    private String fullName;

    private List<AnnualLeaveWorkShiftViewModel> annualLeaves;

    private String tooltipText;

    public EmployeeWorkShiftViewModel() {
        this.annualLeaves = new ArrayList<>();
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

    public List<AnnualLeaveWorkShiftViewModel> getAnnualLeaves() {
        return this.annualLeaves;
    }

    public void setAnnualLeaves(List<AnnualLeaveWorkShiftViewModel> annualLeaves) {
        this.annualLeaves = annualLeaves;
    }

    public String getTooltipText() {
        return this.tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }
}
