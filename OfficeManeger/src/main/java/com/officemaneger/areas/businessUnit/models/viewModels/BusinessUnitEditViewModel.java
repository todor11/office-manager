package com.officemaneger.areas.businessUnit.models.viewModels;

import com.officemaneger.areas.position.models.viewModels.PositionViewInEditBusinessUnitModel;

import java.util.ArrayList;
import java.util.List;

public class BusinessUnitEditViewModel {

    private Long id;

    private String unitName;

    private Long mainUnitId;

    private PositionViewInEditBusinessUnitModel bossPosition;

    private List<PositionViewInEditBusinessUnitModel> positions;

    public BusinessUnitEditViewModel() {
        this.positions = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public Long getMainUnitId() {
        return this.mainUnitId;
    }

    public void setMainUnitId(Long mainUnitId) {
        this.mainUnitId = mainUnitId;
    }

    public PositionViewInEditBusinessUnitModel getBossPosition() {
        return bossPosition;
    }

    public void setBossPosition(PositionViewInEditBusinessUnitModel bossPosition) {
        this.bossPosition = bossPosition;
    }

    public List<PositionViewInEditBusinessUnitModel> getPositions() {
        return positions;
    }

    public void setPositions(List<PositionViewInEditBusinessUnitModel> positions) {
        this.positions = positions;
    }
}
