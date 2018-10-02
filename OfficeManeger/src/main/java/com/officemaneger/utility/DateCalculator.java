package com.officemaneger.utility;

import com.officemaneger.areas.dateInterval.entities.DateInterval;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateCalculator {

    public static Long getNumbOfDaysBetweenDatesIncludingThem(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return null;
        }

        long numbOfDays = ChronoUnit.DAYS.between(startDate, endDate.plusDays(1L));
        if (numbOfDays < 0) {
            numbOfDays = numbOfDays * -1;
        }

        return numbOfDays;
    }

    public static Boolean doesHaveDateIntervalsConflict(DateInterval firstDateInterval, DateInterval secondDateInterval) {
        if (firstDateInterval == null || secondDateInterval == null ||
                firstDateInterval.getStartDate() == null || firstDateInterval.getEndDate() == null ||
                secondDateInterval.getStartDate() == null || secondDateInterval.getEndDate() == null) {
            return null;
        }

        if (firstDateInterval.getStartDate().isEqual(secondDateInterval.getStartDate())) {
            return true;
        } else if (firstDateInterval.getStartDate().isBefore(secondDateInterval.getStartDate())) {
            if (firstDateInterval.getEndDate().isBefore(secondDateInterval.getStartDate())) {
                return false;
            } else {
                return true;
            }
        } else {
            if (secondDateInterval.getEndDate().isBefore(firstDateInterval.getStartDate())) {
                return false;
            } else {
                return true;
            }
        }
    }
}
