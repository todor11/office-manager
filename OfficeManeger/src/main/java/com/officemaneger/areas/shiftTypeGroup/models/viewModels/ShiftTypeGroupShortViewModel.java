package com.officemaneger.areas.shiftTypeGroup.models.viewModels;

public class ShiftTypeGroupShortViewModel {

    private Long id;

    private String groupName;

    private int groupOrderWeight;

    public ShiftTypeGroupShortViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupOrderWeight() {
        return this.groupOrderWeight;
    }

    public void setGroupOrderWeight(int groupOrderWeight) {
        this.groupOrderWeight = groupOrderWeight;
    }
}
