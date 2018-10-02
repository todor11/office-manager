package com.officemaneger.areas.employeeShift.models.viewModels;

import com.officemaneger.areas.dualNameTypes.entities.DualNameType;
import com.officemaneger.areas.employeeShift.models.interfaces.EmployeeShiftViewAll;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class EmployeeShiftViewAllImpl implements EmployeeShiftViewAll {

    private Long id;

    private DualNameType dualNameType;

    private LocalTime shiftDuration;

    private LocalTime breakDuration;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime secondStartTime;

    private LocalDateTime secondEndTime;

    private boolean isEndOnNextDay;

    public EmployeeShiftViewAllImpl() {
    }

    public EmployeeShiftViewAllImpl(Long id) {
        this.id = id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDualNameType(DualNameType dualNameType) {
        this.dualNameType = dualNameType;
    }

    public void setShiftDuration(LocalTime shiftDuration) {
        this.shiftDuration = shiftDuration;
    }

    public void setBreakDuration(LocalTime breakDuration) {
        this.breakDuration = breakDuration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setSecondStartTime(LocalDateTime secondStartTime) {
        this.secondStartTime = secondStartTime;
    }

    public void setSecondEndTime(LocalDateTime secondEndTime) {
        this.secondEndTime = secondEndTime;
    }

    public void setIsEndOnNextDay(boolean isEndOnNextDay) {
        this.isEndOnNextDay = isEndOnNextDay;
    }

    @Override
    public Long getId() {
        return null;
    }

    @Override
    public DualNameType getDualNameType() {
        return null;
    }

    @Override
    public LocalTime getShiftDuration() {
        return null;
    }

    @Override
    public LocalTime getBreakDuration() {
        return null;
    }

    @Override
    public LocalDateTime getStartTime() {
        return null;
    }

    @Override
    public LocalDateTime getEndTime() {
        return null;
    }

    @Override
    public LocalDateTime getSecondStartTime() {
        return null;
    }

    @Override
    public LocalDateTime getSecondEndTime() {
        return null;
    }

    @Override
    public boolean getIsEndOnNextDay() {
        return false;
    }
}
