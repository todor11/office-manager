package com.officemaneger.areas.employeeShift.models.interfaces;

import com.officemaneger.areas.dualNameTypes.entities.DualNameType;

import java.time.LocalDateTime;
import java.time.LocalTime;

public interface EmployeeShiftViewAll {

    Long getId();

    DualNameType getDualNameType();

    LocalTime getShiftDuration();

    LocalTime getBreakDuration();

    LocalDateTime getStartTime();

    LocalDateTime getEndTime();

    LocalDateTime getSecondStartTime();

    LocalDateTime getSecondEndTime();

    boolean getIsEndOnNextDay();
}
