package com.officemaneger.areas.annualLeaveRequest.models.viewModels;

import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalUpdateViewModel;

import java.util.ArrayList;
import java.util.List;

public class AnnualLeaveRequestUpdateViewModel {

    private Long id;

    private List<AnnualLeaveDateIntervalUpdateViewModel> annualLeaveDateIntervals;

    public AnnualLeaveRequestUpdateViewModel() {
        this.setAnnualLeaveDateIntervals(new ArrayList<>());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<AnnualLeaveDateIntervalUpdateViewModel> getAnnualLeaveDateIntervals() {
        return this.annualLeaveDateIntervals;
    }

    public void setAnnualLeaveDateIntervals(List<AnnualLeaveDateIntervalUpdateViewModel> annualLeaveDateIntervals) {
        this.annualLeaveDateIntervals = annualLeaveDateIntervals;
    }
}
