package com.officemaneger.areas.position.models.viewModels;

import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitNameViewModel;
import com.officemaneger.areas.employee.models.viewModels.EmployeeFullNameViewModel;
import com.officemaneger.areas.employee.models.viewModels.EmployeeFullShortNameViewModel;

import java.util.List;

public class PositionViewInEditBusinessUnitModel {

    private Long id;

    private String name;

    private EmployeeFullNameViewModel employee;

    public PositionViewInEditBusinessUnitModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public EmployeeFullNameViewModel getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeFullNameViewModel employee) {
        this.employee = employee;
    }
}
