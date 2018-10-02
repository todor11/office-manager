package com.officemaneger.areas.employeeShiftStatisticContainer;

import com.officemaneger.areas.dualNameTypes.entities.DualNameType;

import java.time.LocalDate;
import java.util.Map;
import java.util.TreeMap;

public class EmployeeWorkingShiftsTemplate {

    private Map<LocalDate, DualNameType> data;

    public EmployeeWorkingShiftsTemplate() {
        this.data = new TreeMap<>();
    }
}
