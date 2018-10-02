package com.officemaneger.areas.employeeShift.models.viewModels;

import com.officemaneger.areas.dualNameTypes.models.DualNameTypeViewModel;

public class EmployeeShiftViewAllModel {

    private long id;

    private String date;

    private String shortDate;

    private String typeAsString;

    private DualNameTypeViewModel type;

    private String tooltipText;

    private String shiftDuration;

    private String breakDuration;

    private String startTime;

    private String endTime;

    private String secondStartTime;

    private String secondEndTime;

    private boolean isEndOnNextDay;

    private boolean isHoliday;

    private boolean isToday;

    private boolean hasCustomTime;

    private boolean isTypicalWorkDay;

    private boolean isTypicalRestDay;

    private boolean isLastWorkDayBeforeRestDay;

    private boolean isLastRestDayBeforeWorkDay;

    public EmployeeShiftViewAllModel() {
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortDate() {
        return this.shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getTypeAsString() {
        return this.typeAsString;
    }

    public void setTypeAsString(String typeAsString) {
        this.typeAsString = typeAsString;
    }

    public DualNameTypeViewModel getType() {
        return this.type;
    }

    public void setType(DualNameTypeViewModel type) {
        this.type = type;
    }

    public String getTooltipText() {
        return this.tooltipText;
    }

    public void setTooltipText(String tooltipText) {
        this.tooltipText = tooltipText;
    }

    public String getShiftDuration() {
        return this.shiftDuration;
    }

    public void setShiftDuration(String shiftDuration) {
        this.shiftDuration = shiftDuration;
    }

    public String getBreakDuration() {
        return this.breakDuration;
    }

    public void setBreakDuration(String breakDuration) {
        this.breakDuration = breakDuration;
    }

    public String getStartTime() {
        return this.startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return this.endTime;
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

    public boolean getIsEndOnNextDay() {
        return this.isEndOnNextDay;
    }

    public void setIsEndOnNextDay(boolean isEndOnNextDay) {
        this.isEndOnNextDay = isEndOnNextDay;
    }

    public boolean getIsHoliday() {
        return this.isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public boolean getIsToday() {
        return this.isToday;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }

    public boolean getHasCustomTime() {
        return this.hasCustomTime;
    }

    public void setHasCustomTime(boolean hasCustomTime) {
        this.hasCustomTime = hasCustomTime;
    }

    public boolean getIsTypicalWorkDay() {
        return this.isTypicalWorkDay;
    }

    public void setIsTypicalWorkDay(boolean isTypicalWorkDay) {
        this.isTypicalWorkDay = isTypicalWorkDay;
    }

    public boolean getIsTypicalRestDay() {
        return this.isTypicalRestDay;
    }

    public void setIsTypicalRestDay(boolean isTypicalRestDay) {
        this.isTypicalRestDay = isTypicalRestDay;
    }

    public boolean getIsLastWorkDayBeforeRestDay() {
        return this.isLastWorkDayBeforeRestDay;
    }

    public void setIsLastWorkDayBeforeRestDay(boolean isLastWorkDayBeforeRestDay) {
        this.isLastWorkDayBeforeRestDay = isLastWorkDayBeforeRestDay;
    }

    public boolean getIsLastRestDayBeforeWorkDay() {
        return this.isLastRestDayBeforeWorkDay;
    }

    public void setIsLastRestDayBeforeWorkDay(boolean isLastRestDayBeforeWorkDay) {
        this.isLastRestDayBeforeWorkDay = isLastRestDayBeforeWorkDay;
    }
}
