package com.officemaneger.areas.shiftType.models.viewModels;

public class ShiftTypeInWorkShiftViewModel {

    private Long id;

    private String fullName;

    private String shortName;

    private String shiftDuration;

    private String breakDuration;

    private String startTime;

    private String endTime;

    private String secondStartTime;

    private String secondEndTime;

    private boolean isRegularShift;

    private boolean isEndOnNextDay;

    private boolean canBeOnWorkDay;

    private boolean canBeOnRestDay;

    private boolean canBeOnLastWorkDayBeforeRestDay;

    private boolean canBeOnLastRestDayBeforeWorkDay;

    private boolean canBeOnDoubleShiftRegime;

    private boolean canBeOnTripleShiftRegime;

    public ShiftTypeInWorkShiftViewModel() {
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

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShiftDuration() {
        return shiftDuration;
    }

    public void setShiftDuration(String shiftDuration) {
        this.shiftDuration = shiftDuration;
    }

    public String getBreakDuration() {
        return breakDuration;
    }

    public void setBreakDuration(String breakDuration) {
        this.breakDuration = breakDuration;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getSecondStartTime() {
        return this.secondStartTime;
    }

    public void setSecondStartTime(String secondStartTime) {
        this.secondStartTime = secondStartTime;
    }

    public String getSecondEndTime() {
        return this.secondEndTime;
    }

    public void setSecondEndTime(String secondEndTime) {
        this.secondEndTime = secondEndTime;
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
