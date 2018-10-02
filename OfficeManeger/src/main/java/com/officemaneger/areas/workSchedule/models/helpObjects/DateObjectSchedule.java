package com.officemaneger.areas.workSchedule.models.helpObjects;

import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.utility.TimeCalculator;

import java.time.LocalDate;

public class DateObjectSchedule {

    private String date;

    private String shortDate;

    private String shortDateCl;

    private String htmlText;

    private boolean isHoliday;

    private boolean isToday;

    public DateObjectSchedule(LocalDate date) {
        this.init(date);
    }

    public DateObjectSchedule(LocalDate date, WorkDay workDay) {
        this.init(date, workDay);
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortDate() {
        return this.shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public String getShortDateCl() {
        return this.shortDateCl;
    }

    public void setShortDateCl(String shortDateCl) {
        this.shortDateCl = shortDateCl;
    }

    public boolean getIsHoliday() {
        return this.isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public boolean getIsToday() {
        return this.isToday;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }

    public String getHtmlText() {
        return this.htmlText;
    }

    public void setHtmlText(String htmlText) {
        this.htmlText = htmlText;
    }

    private void init(LocalDate date) {
        this.setDate(TimeCalculator.getDateAsString(date, "."));
        this.setShortDate(TimeCalculator.getShortDateAsString(date, "."));
        this.setShortDateCl(TimeCalculator.getShortDateAsString(date, "-"));
        this.setIsToday(date.equals(LocalDate.now()));
        this.setIsHoliday(false);
        this.setHtmlText(this.shortDate + " " + TimeCalculator.getBGDayOfWeekFull(date));
    }

    private void init(LocalDate date, WorkDay workDay) {
        this.init(date);
        this.setIsHoliday(!TimeCalculator.getIsWorkingDayFromWorkDay(workDay));
    }
}
