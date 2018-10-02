package com.officemaneger.areas.workShift.models.viewModels;

import com.officemaneger.areas.employee.models.viewModels.EmployeeWorkShiftViewModel;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeShiftViewAllModel;

import java.util.ArrayList;
import java.util.List;

public class WorkShiftViewAllModel {

    private EmployeeWorkShiftViewModel employee;

    private List<EmployeeShiftViewAllModel> employeeShifts;

    public WorkShiftViewAllModel() {
        this.employeeShifts = new ArrayList<>();
    }

    public EmployeeWorkShiftViewModel getEmployee() {
        return this.employee;
    }

    public void setEmployee(EmployeeWorkShiftViewModel employee) {
        this.employee = employee;
    }

    public List<EmployeeShiftViewAllModel> getEmployeeShifts() {
        return this.employeeShifts;
    }

    public void setEmployeeShifts(List<EmployeeShiftViewAllModel> employeeShifts) {
        this.employeeShifts = employeeShifts;
    }
}
