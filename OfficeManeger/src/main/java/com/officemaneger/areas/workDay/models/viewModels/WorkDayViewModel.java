package com.officemaneger.areas.workDay.models.viewModels;

import java.time.LocalDate;

public class WorkDayViewModel {

    private long id;

    private LocalDate date;

    public WorkDayViewModel() {
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
