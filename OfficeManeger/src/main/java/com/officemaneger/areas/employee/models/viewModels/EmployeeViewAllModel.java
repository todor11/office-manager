package com.officemaneger.areas.employee.models.viewModels;

public class EmployeeViewAllModel {

    private Long id;

    private String fullName;

    private String officiaryID;

    private String rankName;

    private String businessUnitName;

    private boolean isActive;

    public EmployeeViewAllModel() {
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

    public String getOfficiaryID() {
        return this.officiaryID;
    }

    public void setOfficiaryID(String officiaryID) {
        this.officiaryID = officiaryID;
    }

    public String getRankName() {
        return this.rankName;
    }

    public void setRankName(String rankName) {
        this.rankName = rankName;
    }

    public String getBusinessUnitName() {
        return this.businessUnitName;
    }

    public void setBusinessUnitName(String businessUnitName) {
        this.businessUnitName = businessUnitName;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
