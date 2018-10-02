package com.officemaneger.areas.shiftType.entities;

import com.officemaneger.areas.dualNameTypes.entities.DualNameType;
import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@DiscriminatorValue(value = "ShiftType")
public class ShiftType extends DualNameType {

    @ManyToOne
    @JoinColumn(name = "shift_type_group_id", referencedColumnName = "id")
    private ShiftTypeGroup shiftTypeGroup;

    @Column(name = "shift_duration")
    private LocalTime shiftDuration;

    @Column(name = "break_duration")
    private LocalTime breakDuration;

    @Column(name = "start_time")
    private LocalTime startTime;

    @Column(name = "end_time")
    private LocalTime endTime;

    @Column(name = "second_start_time")
    private LocalTime secondStartTime;

    @Column(name = "second_end_time")
    private LocalTime secondEndTime;

    @Column(name = "is_end_on_next_day")
    private boolean isEndOnNextDay;

    @Column(name = "is_regular_shift")
    private boolean isRegularShift;

    @Column(name = "can_be_on_work_day")
    private boolean canBeOnWorkDay;

    @Column(name = "can_be_on_rest_day")
    private boolean canBeOnRestDay;

    @Column(name = "can_be_on_last_work_day_before_rest_day")
    private boolean canBeOnLastWorkDayBeforeRestDay;

    @Column(name = "can_be_on_last_rest_day_before_work_day")
    private boolean canBeOnLastRestDayBeforeWorkDay;

    @Column(name = "can_be_on_double_shift_regime")
    private boolean canBeOnDoubleShiftRegime;

    @Column(name = "can_be_on_triple_shift_regime")
    private boolean canBeOnTripleShiftRegime;

    public ShiftType() {
    }

    public ShiftTypeGroup getShiftTypeGroup() {
        return this.shiftTypeGroup;
    }

    public void setShiftTypeGroup(ShiftTypeGroup shiftTypeGroup) {
        this.shiftTypeGroup = shiftTypeGroup;
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

    public boolean getIsEndOnNextDay() {
        return isEndOnNextDay;
    }

    public void setIsEndOnNextDay(boolean isEndOnNextDay) {
        this.isEndOnNextDay = isEndOnNextDay;
    }

    public boolean getIsRegularShift() {
        return this.isRegularShift;
    }

    public void setIsRegularShift(boolean isRegularShift) {
        this.isRegularShift = isRegularShift;
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
