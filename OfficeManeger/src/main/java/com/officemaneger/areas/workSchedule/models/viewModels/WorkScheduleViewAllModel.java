package com.officemaneger.areas.workSchedule.models.viewModels;

import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupScheduleViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkScheduleViewAllModel {

    private ShiftTypeGroupScheduleViewModel shiftTypeGroup;

    private List<WorkShiftByGroupAndDateViewModel> shiftsByGroupAndDate;

    public WorkScheduleViewAllModel() {
        this.shiftsByGroupAndDate = new ArrayList<>();
    }

    public ShiftTypeGroupScheduleViewModel getShiftTypeGroup() {
        return this.shiftTypeGroup;
    }

    public void setShiftTypeGroup(ShiftTypeGroupScheduleViewModel shiftTypeGroup) {
        this.shiftTypeGroup = shiftTypeGroup;
    }

    public List<WorkShiftByGroupAndDateViewModel> getShiftsByGroupAndDate() {
        return this.shiftsByGroupAndDate;
    }

    public void setShiftsByGroupAndDate(List<WorkShiftByGroupAndDateViewModel> shiftsByGroupAndDate) {
        this.shiftsByGroupAndDate = shiftsByGroupAndDate;
    }
}
