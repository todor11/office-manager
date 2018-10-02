package com.officemaneger.areas.businessUnit.models.viewModels;

import com.officemaneger.areas.position.models.viewModels.PositionNameViewModel;

public class BusinessUnitViewModel {

    private Long id;

    private String unitName;

    private String mainUnit;

    private String boss;

    public BusinessUnitViewModel() {
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

    public String getBoss() {
        return boss;
    }

    public void setBoss(String boss) {
        this.boss = boss;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
