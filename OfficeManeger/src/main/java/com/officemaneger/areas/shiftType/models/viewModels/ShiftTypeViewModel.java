package com.officemaneger.areas.shiftType.models.viewModels;

import java.time.LocalTime;

public class ShiftTypeViewModel {

    private Long id;
    
    private Long shiftTypeGroupId;

    private String fullName;

    private String shortName;

    private LocalTime shiftDuration;

    private LocalTime breakDuration;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime secondStartTime;

    private LocalTime secondEndTime;

    private boolean isRegularShift;

    private boolean isEndOnNextDay;

    private boolean canBeOnWorkDay;

    private boolean canBeOnRestDay;

    private boolean canBeOnDoubleShiftRegime;

    private boolean canBeOnTripleShiftRegime;

    //private boolean canBeOnFirstWorkDayAfterRestDay;
//
    //private boolean canBeOnFirstRestDayAfterWorkDay;

    private boolean canBeOnLastWorkDayBeforeRestDay;

    private boolean canBeOnLastRestDayBeforeWorkDay;

    public ShiftTypeViewModel() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShiftTypeGroupId() {
        return this.shiftTypeGroupId;
    }

    public void setShiftTypeGroupId(Long shiftTypeGroupId) {
        this.shiftTypeGroupId = shiftTypeGroupId;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public LocalTime getShiftDuration() {
        return shiftDuration;
    }

    public void setShiftDuration(LocalTime shiftDuration) {
        this.shiftDuration = shiftDuration;
    }

    public LocalTime getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(LocalTime breakDuration) {
        this.breakDuration = breakDuration;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public boolean getIsRegularShift() {
        return this.isRegularShift;
    }

    public void setIsRegularShift(boolean isRegularShift) {
        this.isRegularShift = isRegularShift;
    }

    public boolean getIsEndOnNextDay() {
        return isEndOnNextDay;
    }

    public void setIsEndOnNextDay(boolean isEndOnNextDay) {
        this.isEndOnNextDay = isEndOnNextDay;
    }

    public LocalTime getSecondStartTime() {
        return this.secondStartTime;
    }

    public void setSecondStartTime(LocalTime secondStartTime) {
        this.secondStartTime = secondStartTime;
    }

    public LocalTime getSecondEndTime() {
        return this.secondEndTime;
    }

    public void setSecondEndTime(LocalTime secondEndTime) {
        this.secondEndTime = secondEndTime;
    }

    public boolean getCanBeOnWorkDay() {
        return this.canBeOnWorkDay;
    }

    public void setCanBeOnWorkDay(boolean canBeOnWorkDay) {
        this.canBeOnWorkDay = canBeOnWorkDay;
    }

    public boolean getCanBeOnRestDay() {
        return this.canBeOnRestDay;
    }

    public void setCanBeOnRestDay(boolean canBeOnRestDay) {
        this.canBeOnRestDay = canBeOnRestDay;
    }

    //public boolean getCanBeOnFirstWorkDayAfterRestDay() {
    //    return this.canBeOnFirstWorkDayAfterRestDay;
    //}
//
    //public void setCanBeOnFirstWorkDayAfterRestDay(boolean canBeOnFirstWorkDayAfterRestDay) {
    //    this.canBeOnFirstWorkDayAfterRestDay = canBeOnFirstWorkDayAfterRestDay;
    //}
//
    //public boolean getCanBeOnFirstRestDayAfterWorkDay() {
    //    return this.canBeOnFirstRestDayAfterWorkDay;
    //}
//
    //public void setCanBeOnFirstRestDayAfterWorkDay(boolean canBeOnFirstRestDayAfterWorkDay) {
    //    this.canBeOnFirstRestDayAfterWorkDay = canBeOnFirstRestDayAfterWorkDay;
    //}

    public boolean getCanBeOnDoubleShiftRegime() {
        return this.canBeOnDoubleShiftRegime;
    }

    public void setCanBeOnDoubleShiftRegime(boolean canBeOnDoubleShiftRegime) {
        this.canBeOnDoubleShiftRegime = canBeOnDoubleShiftRegime;
    }

    public boolean getCanBeOnTripleShiftRegime() {
        return this.canBeOnTripleShiftRegime;
    }

    public void setCanBeOnTripleShiftRegime(boolean canBeOnTripleShiftRegime) {
        this.canBeOnTripleShiftRegime = canBeOnTripleShiftRegime;
    }

    public boolean getCanBeOnLastWorkDayBeforeRestDay() {
        return this.canBeOnLastWorkDayBeforeRestDay;
    }

    public void setCanBeOnLastWorkDayBeforeRestDay(boolean canBeOnLastWorkDayBeforeRestDay) {
        this.canBeOnLastWorkDayBeforeRestDay = canBeOnLastWorkDayBeforeRestDay;
    }

    public boolean getCanBeOnLastRestDayBeforeWorkDay() {
        return this.canBeOnLastRestDayBeforeWorkDay;
    }

    public void setCanBeOnLastRestDayBeforeWorkDay(boolean canBeOnLastRestDayBeforeWorkDay) {
        this.canBeOnLastRestDayBeforeWorkDay = canBeOnLastRestDayBeforeWorkDay;
    }
}
