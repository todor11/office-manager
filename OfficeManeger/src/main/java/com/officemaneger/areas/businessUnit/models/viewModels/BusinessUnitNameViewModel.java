package com.officemaneger.areas.businessUnit.models.viewModels;

public class BusinessUnitNameViewModel {

    private Long id;

    private String unitName;

    public BusinessUnitNameViewModel() {
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
