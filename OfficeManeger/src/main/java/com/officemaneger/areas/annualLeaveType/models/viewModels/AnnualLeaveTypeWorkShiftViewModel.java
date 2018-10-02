package com.officemaneger.areas.annualLeaveType.models.viewModels;

import java.util.ArrayList;
import java.util.List;

public class AnnualLeaveTypeWorkShiftViewModel {

    private Long id;

    private String fullName;

    private String shortName;

    private List<Long> employeeIds;

    private List<Long> annualLeaveIds;

    public AnnualLeaveTypeWorkShiftViewModel() {
        this.employeeIds = new ArrayList<>();
        this.annualLeaveIds = new ArrayList<>();
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public List<Long> getEmployeeIds() {
        return this.employeeIds;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public List<Long> getAnnualLeaveIds() {
        return this.annualLeaveIds;
    }

    public void setAnnualLeaveIds(List<Long> annualLeaveIds) {
        this.annualLeaveIds = annualLeaveIds;
    }
}
