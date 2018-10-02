package com.officemaneger.areas.shiftType.models.bindingModels;

import org.hibernate.validator.constraints.NotEmpty;

import java.time.LocalTime;

public class ShiftTypeCreationModel {

    private Long shiftTypeGroupId;

    @NotEmpty(message = "error.title.notnull.fullName")
    private String fullName;

    @NotEmpty(message = "error.title.notnull.shortName")
    private String shortName;

    private LocalTime shiftDuration;

    private LocalTime breakDuration;

    private LocalTime startTime;

    private LocalTime endTime;

    private LocalTime secondStartTime;

    private LocalTime secondEndTime;

    private boolean isRegularShift;

    private boolean isEndOnNextDay;

    private boolean isActive;

    private boolean canBeOnWorkDay;

    private boolean canBeOnRestDay;

    private boolean canBeOnLastWorkDayBeforeRestDay;

    private boolean canBeOnLastRestDayBeforeWorkDay;

    private boolean canBeOnDoubleShiftRegime;

    private boolean canBeOnTripleShiftRegime;

    public ShiftTypeCreationModel() {
    }

    public Long getShiftTypeGroupId() {
        return this.shiftTypeGroupId;
    }

    public void setShiftTypeGroupId(Long shiftTypeGroupId) {
        this.shiftTypeGroupId = shiftTypeGroupId;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return this.shortName;
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

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
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
