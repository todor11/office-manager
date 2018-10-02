package com.officemaneger.areas.businessUnit.models.bindingModels;

import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

public class BusinessUnitCreationModel {

    @NotEmpty(message = "Името на структорната единица е задължително")
    private String unitName;

    private String mainUnit;

    private String bossPosition;

    private Long bossId;

    private List<String> employeesPositions;

    private List<Long> employeesIds;

    public BusinessUnitCreationModel() {
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(String mainUnit) {
        this.mainUnit = mainUnit;
    }

    public String getBossPosition() {
        return bossPosition;
    }

    public void setBossPosition(String bossPosition) {
        this.bossPosition = bossPosition;
    }

    public List<String> getEmployeesPositions() {
        return employeesPositions;
    }

    public void setEmployeesPositions(List<String> employeesPositions) {
        this.employeesPositions = employeesPositions;
    }

    public Long getBossId() {
        return bossId;
    }

    public void setBossId(Long bossId) {
        this.bossId = bossId;
    }

    public List<Long> getEmployeesIds() {
        return employeesIds;
    }

    public void setEmployeesIds(List<Long> employeesIds) {
        this.employeesIds = employeesIds;
    }
}
