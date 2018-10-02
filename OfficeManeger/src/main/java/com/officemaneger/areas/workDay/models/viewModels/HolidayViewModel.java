package com.officemaneger.areas.workDay.models.viewModels;

import java.time.LocalDate;

public class HolidayViewModel {

    private long id;

    private LocalDate date;

    public HolidayViewModel() {
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
