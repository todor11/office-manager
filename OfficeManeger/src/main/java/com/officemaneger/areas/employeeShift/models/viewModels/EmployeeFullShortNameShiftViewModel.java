package com.officemaneger.areas.employeeShift.models.viewModels;

public class EmployeeFullShortNameShiftViewModel {

    private Long employeeId;

    private Long employeeShiftId;

    private String employeeFullShortName;

    public EmployeeFullShortNameShiftViewModel() {
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public Long getEmployeeShiftId() {
        return this.employeeShiftId;
    }

    public void setEmployeeShiftId(Long employeeShiftId) {
        this.employeeShiftId = employeeShiftId;
    }

    public String getEmployeeFullShortName() {
        return this.employeeFullShortName;
    }

    public void setEmployeeFullShortName(String employeeFullShortName) {
        this.employeeFullShortName = employeeFullShortName;
    }
}
