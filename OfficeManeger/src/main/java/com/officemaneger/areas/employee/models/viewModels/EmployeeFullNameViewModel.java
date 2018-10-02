package com.officemaneger.areas.employee.models.viewModels;

public class EmployeeFullNameViewModel {

    private Long id;

    private String fullName;

    public EmployeeFullNameViewModel() {
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
}
