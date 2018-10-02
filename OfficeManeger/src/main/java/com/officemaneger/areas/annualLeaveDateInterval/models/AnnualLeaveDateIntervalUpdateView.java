package com.officemaneger.areas.annualLeaveDateInterval.models;

import java.time.LocalDate;

public interface AnnualLeaveDateIntervalUpdateView {

    Long getId();

    LocalDate getStartDate();

    LocalDate getEndDate();

    Long getShiftTypeId();
}
