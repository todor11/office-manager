package com.officemaneger.areas.position.models.viewModels;

import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitNameViewModel;
import com.officemaneger.areas.employee.models.viewModels.EmployeeFullShortNameViewModel;

import java.util.List;

public class PositionViewModel {

    private String name;

    private BusinessUnitNameViewModel businessUnit;

    private EmployeeFullShortNameViewModel employee;

    public PositionViewModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusinessUnitNameViewModel getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnitNameViewModel businessUnit) {
        this.businessUnit = businessUnit;
    }

    public EmployeeFullShortNameViewModel getEmployee() {
        return employee;
    }

    public void setEmployee(EmployeeFullShortNameViewModel employee) {
        this.employee = employee;
    }
}
