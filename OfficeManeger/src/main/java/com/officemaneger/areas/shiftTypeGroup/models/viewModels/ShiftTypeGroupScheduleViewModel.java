package com.officemaneger.areas.shiftTypeGroup.models.viewModels;

import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeViewModel;

import java.util.List;

public class ShiftTypeGroupScheduleViewModel {

    private Long id;

    private String groupName;

    private List<ShiftTypeViewModel> shiftTypes;

    private int groupOrderWeight;

    public ShiftTypeGroupScheduleViewModel() {
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

    public List<ShiftTypeViewModel> getShiftTypes() {
        return this.shiftTypes;
    }

    public void setShiftTypes(List<ShiftTypeViewModel> shiftTypes) {
        this.shiftTypes = shiftTypes;
    }

    public int getGroupOrderWeight() {
        return this.groupOrderWeight;
    }

    public void setGroupOrderWeight(int groupOrderWeight) {
        this.groupOrderWeight = groupOrderWeight;
    }

/*
    public ShiftTypeViewModel getShiftThatCanBeOnWorkDayDoubleRegime() {
        for (ShiftTypeViewModel shiftType : this.shiftTypes) {
            if (shiftType.getCanBeOnDoubleShiftRegime() && shiftType.getCanBeOnWorkDay()) {
                return shiftType;
            }
        }

        return null;
    }

    public ShiftTypeViewModel getShiftThatCanBeOnRestDayDoubleRegime() {
        for (ShiftTypeViewModel shiftType : this.shiftTypes) {
            if (shiftType.getCanBeOnDoubleShiftRegime() && shiftType.getCanBeOnRestDay()) {
                return shiftType;
            }
        }

        return null;
    }
    */
}
