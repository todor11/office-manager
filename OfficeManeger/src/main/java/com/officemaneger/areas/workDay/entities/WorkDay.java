package com.officemaneger.areas.workDay.entities;

import com.officemaneger.enums.WorkingDayType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "workdays")
public class WorkDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private LocalDate date;

    @Column(name = "is_rest_day_of_week")
    private boolean isRestDayOfWeek;

    @Enumerated(EnumType.STRING)
    private WorkingDayType workingDayType;

    public WorkDay() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean getIsRestDayOfWeek() {
        return isRestDayOfWeek;
    }

    public void setIsRestDayOfWeek(boolean isRestDayOfWeek) {
        this.isRestDayOfWeek = isRestDayOfWeek;
    }

    public WorkingDayType getWorkingDayType() {
        return workingDayType;
    }

    public void setWorkingDayType(WorkingDayType workingDayType) {
        this.workingDayType = workingDayType;
    }
}