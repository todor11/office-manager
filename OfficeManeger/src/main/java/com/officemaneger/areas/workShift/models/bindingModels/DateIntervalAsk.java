package com.officemaneger.areas.workShift.models.bindingModels;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class DateIntervalAsk {

    @NotNull(message = "Началната дата не може да е празно поле")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate startDate;

    @NotNull(message = "Крайната дата не може да е празно поле")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate endDate;

    public DateIntervalAsk() {
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
