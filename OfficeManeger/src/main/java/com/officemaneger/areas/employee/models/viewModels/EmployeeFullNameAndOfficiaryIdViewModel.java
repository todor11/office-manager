package com.officemaneger.areas.employee.models.viewModels;

public class EmployeeFullNameAndOfficiaryIdViewModel {

    private Long id;

    private String fullNameAndOfficiaryId;

    public EmployeeFullNameAndOfficiaryIdViewModel() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullNameAndOfficiaryId() {
        return this.fullNameAndOfficiaryId;
    }

    public void setFullNameAndOfficiaryId(String fullNameAndOfficiaryId) {
        this.fullNameAndOfficiaryId = fullNameAndOfficiaryId;
    }
}
