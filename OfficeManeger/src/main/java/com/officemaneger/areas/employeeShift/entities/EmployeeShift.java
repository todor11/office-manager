package com.officemaneger.areas.employeeShift.entities;

import com.officemaneger.areas.dualNameTypes.entities.DualNameType;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.enums.WorkingDayType;
//import com.officemaneger.areas.workShift.entities.WorkShift;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "employee_shifts", uniqueConstraints = { @UniqueConstraint(columnNames =
        { "owner_id", "date" }) })
public class EmployeeShift implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private DualNameType type;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Employee owner;

    private LocalDate date;

    @ManyToOne
    @JoinColumn(name = "work_day_id", referencedColumnName = "id")
    private WorkDay workDay;

    @Column(name = "shift_duration")
    private LocalTime shiftDuration;

    @Column(name = "break_duration")
    private LocalTime breakDuration;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "second_start_time")
    private LocalDateTime secondStartTime;

    @Column(name = "second_end_time")
    private LocalDateTime secondEndTime;

    @Column(name = "is_end_on_next_day")
    private boolean isEndOnNextDay;

    @Column(name = "has_custom_time")
    private boolean hasCustomTime;

    //@Column(name = "paid_time_in_work_shift")
    //private LocalTime paidTimeInWorkShift;

    public EmployeeShift() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public DualNameType getType() {
        return type;
    }

    public void setType(DualNameType type) {
        this.type = type;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public WorkDay getWorkDay() {
        return workDay;
    }

    public void setWorkDay(WorkDay workDay) {
        this.workDay = workDay;
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

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getSecondStartTime() {
        return secondStartTime;
    }

    public void setSecondStartTime(LocalDateTime secondStartTime) {
        this.secondStartTime = secondStartTime;
    }

    public LocalDateTime getSecondEndTime() {
        return secondEndTime;
    }

    public void setSecondEndTime(LocalDateTime secondEndTime) {
        this.secondEndTime = secondEndTime;
    }

    public boolean getIsEndOnNextDay() {
        return this.isEndOnNextDay;
    }

    public void setIsEndOnNextDay(boolean isEndOnNextDay) {
        this.isEndOnNextDay = isEndOnNextDay;
    }

    public boolean getHasCustomTime() {
        return this.hasCustomTime;
    }

    public void setHasCustomTime(boolean hasCustomTime) {
        this.hasCustomTime = hasCustomTime;
    }

    //public LocalTime getPaidTimeInWorkShift() {
    //    return this.paidTimeInWorkShift;
    //}
//
    //public void setPaidTimeInWorkShift(LocalTime paidTimeInWorkShift) {
    //    this.paidTimeInWorkShift = paidTimeInWorkShift;
    //}
}
