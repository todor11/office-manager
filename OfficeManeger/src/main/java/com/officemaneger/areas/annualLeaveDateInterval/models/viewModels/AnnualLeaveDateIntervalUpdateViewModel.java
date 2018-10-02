package com.officemaneger.areas.annualLeaveDateInterval.models.viewModels;

import java.time.LocalDate;

public class AnnualLeaveDateIntervalUpdateViewModel {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Long shiftTypeId;

    private int numbOfAnnualLeave;

    public AnnualLeaveDateIntervalUpdateViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getShiftTypeId() {
        return this.shiftTypeId;
    }

    public void setShiftTypeId(Long shiftTypeId) {
        this.shiftTypeId = shiftTypeId;
    }

    public int getNumbOfAnnualLeave() {
        return this.numbOfAnnualLeave;
    }

    public void setNumbOfAnnualLeave(int numbOfAnnualLeave) {
        this.numbOfAnnualLeave = numbOfAnnualLeave;
    }
}
