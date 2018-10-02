package com.officemaneger.areas.employee.models.viewModels;

import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAnnualLeavesViewModel {

    private Long id;

    private String fullName;

    private List<AnnualLeaveEmployeeViewModel> annualLeaves;

    public EmployeeAnnualLeavesViewModel() {
        this.annualLeaves = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public List<AnnualLeaveEmployeeViewModel> getAnnualLeaves() {
        return this.annualLeaves;
    }

    public void setAnnualLeaves(List<AnnualLeaveEmployeeViewModel> annualLeaves) {
        this.annualLeaves = annualLeaves;
    }
}
