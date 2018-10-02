package com.officemaneger.areas.workShift.models.heplObjects;

import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.utility.TimeCalculator;

import java.time.LocalDate;

public class DateObject {

    private String date;

    private String shortDate;

    private String shortDateCl;

    private String htmlText;

    private boolean isHoliday;

    private boolean isToday;

    private boolean isWorkDay;

    private boolean isRestDay;

    private boolean isLastWorkDayBeforeRestDay;

    private boolean isLastRestDayBeforeWorkDay;

    public DateObject(WorkDay workDay, WorkDay nextWorkDay) {
        this.init(workDay, nextWorkDay);
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

    public boolean getIsWorkDay() {
        return this.isWorkDay;
    }

    public void setIsWorkDay(boolean isWorkDay) {
        this.isWorkDay = isWorkDay;
    }

    public boolean getIsRestDay() {
        return this.isRestDay;
    }

    public void setIsRestDay(boolean isRestDay) {
        this.isRestDay = isRestDay;
    }

    public boolean getIsLastWorkDayBeforeRestDay() {
        return this.isLastWorkDayBeforeRestDay;
    }

    public void setIsLastWorkDayBeforeRestDay(boolean isLastWorkDayBeforeRestDay) {
        this.isLastWorkDayBeforeRestDay = isLastWorkDayBeforeRestDay;
    }

    public boolean getIsLastRestDayBeforeWorkDay() {
        return this.isLastRestDayBeforeWorkDay;
    }

    public void setIsLastRestDayBeforeWorkDay(boolean isLastRestDayBeforeWorkDay) {
        this.isLastRestDayBeforeWorkDay = isLastRestDayBeforeWorkDay;
    }

    private void init(LocalDate date) {
        this.setDate(TimeCalculator.getDateAsString(date, "."));
        this.setShortDate(TimeCalculator.getShortDateAsString(date, "."));
        this.setShortDateCl(TimeCalculator.getShortDateAsString(date, "-"));
        this.setIsToday(date.equals(LocalDate.now()));
        this.setIsHoliday(false);
        this.setHtmlText(this.shortDate + " " + TimeCalculator.getBGDayOfWeekShort(date));
    }

    private void init(WorkDay workDay, WorkDay nextWorkDay) {
        this.init(workDay.getDate());
        boolean isHoliday = !TimeCalculator.getIsWorkingDayFromWorkDay(workDay);
        this.setIsHoliday(isHoliday);
        this.setIsWorkDay(false);
        this.setIsRestDay(false);
        this.setIsLastWorkDayBeforeRestDay(false);
        this.setIsLastRestDayBeforeWorkDay(false);
        boolean isNextDayHoliday = !TimeCalculator.getIsWorkingDayFromWorkDay(nextWorkDay);
        if (isHoliday) {
            if (isNextDayHoliday) {
                this.setIsRestDay(true);
            } else {
                this.setIsLastRestDayBeforeWorkDay(true);
            }
        } else {
            if (isNextDayHoliday) {
                this.setIsLastWorkDayBeforeRestDay(true);
            } else {
                this.setIsWorkDay(true);
            }
        }
    }
}
