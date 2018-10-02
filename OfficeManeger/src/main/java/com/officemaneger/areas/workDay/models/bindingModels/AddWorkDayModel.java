package com.officemaneger.areas.workDay.models.bindingModels;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.officemaneger.enums.WorkingDayType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDate;

public class AddWorkDayModel {

    @NotNull(message = "error.title.notnull")
    @JsonFormat(pattern = "dd.MM.yyyy")
    private LocalDate date;

    private boolean isRestDayOfWeek;

    private final WorkingDayType workingDayType = WorkingDayType.WORKDAY;

    public AddWorkDayModel() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public WorkingDayType getWorkingDayType() {
        return workingDayType;
    }

    public void setIsRestDayOfWeek() {
        if (this.date != null && (this.date.getDayOfWeek().getValue() == 7 ||
                this.date.getDayOfWeek().getValue() == 6)) {
            this.isRestDayOfWeek = true;
        }
    }

    public boolean getIsRestDayOfWeek() {
        return isRestDayOfWeek;
    }

    public void setIsRestDayOfWeek(boolean isRestDayOfWeek) {
        this.isRestDayOfWeek = isRestDayOfWeek;
    }
}
