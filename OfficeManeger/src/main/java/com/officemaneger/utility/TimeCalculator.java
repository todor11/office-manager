package com.officemaneger.utility;

import com.officemaneger.areas.employeeShift.entities.EmployeeShift;
import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.enums.WorkingDayType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimeCalculator {

    public static LocalTime getTimeMinusTime(LocalTime startTime, LocalTime endTime) {
        if (startTime == null || endTime == null) {
            return null;
        }

        LocalTime resultTime = null;

        if (startTime.isBefore(endTime)) {
            int[] array = getDuration(startTime.getHour(), startTime.getMinute(), endTime.getHour(), endTime.getMinute());
            resultTime = LocalTime.of(array[0], array[1]);
        } else {
            int[] firstDayTime = getDuration(startTime.getHour(), startTime.getMinute(), 24, 0);

            int resultHour = firstDayTime[0] + endTime.getHour();
            int resultMinute = firstDayTime[1] + endTime.getMinute();
            if (resultMinute >= 60) {
                resultMinute -= 60;
                resultHour += 1;
            }

            resultTime = LocalTime.of(resultHour, resultMinute);
        }

        return resultTime;
    }

    public static int[] getTimePlusTimeIntArr(LocalTime firstTime, LocalTime secondTime) {
        if (firstTime == null || secondTime == null) {
            return null;
        }

        int[] result = new int[3];
        int firstHour = firstTime.getHour();
        int firstMinute = firstTime.getMinute();
        int secondHour = secondTime.getHour();
        int secondMinute = secondTime.getMinute();

        int resultDays = 0;
        int resultHours = firstHour + secondHour;
        int resultMinute = firstMinute + secondMinute;

        if (resultMinute >= 60) {
            resultMinute -= 60;
            resultHours++;
        }

        if (resultHours >= 24) {
            resultHours -= 24;
            resultDays = 1;
        }

        result[0] = resultDays;
        result[1] = resultHours;
        result[2] = resultMinute;

        return result;
    }

    private static int[] getDuration(int startHour, int startMinute, int endHour, int endMinute) {
        int[] result = new int[2];
        if ((startHour == endHour && startMinute < endMinute) || startHour < endHour) {
            int resultMinutes = endMinute - startMinute;
            int resultHours = endHour - startHour;
            int bufferHour = 0;
            if (resultMinutes < 0) {
                bufferHour = 1;
                resultMinutes = 60 + resultMinutes;
            }

            resultHours -= bufferHour;
            result[0] = resultHours;
            result[1] = resultMinutes;
        }

        return result;
    }

    public static LocalTime getTimePlusTime(LocalTime firstTime, LocalTime secondTime) {
        if (firstTime == null || secondTime == null) {
            return null;
        }

        int[] resultArr = TimeCalculator.getTimePlusTimeIntArr(firstTime, secondTime);
        return LocalTime.of(resultArr[1], resultArr[2]);
    }

    public static LocalDateTime getDateTimeMinusTime(LocalDateTime dateTime, LocalTime minusTime) {
        if (dateTime == null || minusTime == null) {
            return null;
        }

        LocalDateTime resultDateTime = dateTime.minusSeconds(minusTime.getSecond());
        resultDateTime = resultDateTime.minusMinutes(minusTime.getMinute());
        resultDateTime = resultDateTime.minusHours(minusTime.getHour());

        return resultDateTime;
    }

    public static LocalTime getTimeFromDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return LocalTime.of(dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
    }

    public static String getTimeAsString(LocalTime time) {
        if (time == null) {
            return null;
        }

        int minute = time.getMinute();
        String minuteAsStr = "" + minute;
        if (minute < 10) {
            minuteAsStr = "0" + minuteAsStr;
        }
        int hour = time.getHour();
        String hourAsStr = "" + hour;
        if (hour < 10) {
            hourAsStr = "0" + hourAsStr;
        }

        return hourAsStr + ":" + minuteAsStr;
    }


    public static String getTimeAsStringFromDateTime(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        return TimeCalculator.getTimeAsString(TimeCalculator.getTimeFromDateTime(dateTime));
    }

    public static String getDateTimeAsString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return null;
        }

        int day = dateTime.getDayOfMonth();
        String dayAsString = String.valueOf(day);
        if (day < 10) {
            dayAsString = "0" + dayAsString;
        }

        int month = dateTime.getMonthValue();
        String monthAsString = String.valueOf(month);
        if (month < 10) {
            monthAsString = "0" + monthAsString;
        }

        int year = dateTime.getYear();
        String yearAsString = String.valueOf(year);

        int hour = dateTime.getHour();
        String hourAsString = String.valueOf(hour);
        if (hour < 10) {
            hourAsString = "0" + hourAsString;
        }

        int minute = dateTime.getMinute();
        String minuteAsString = String.valueOf(minute);
        if (minute < 10) {
            minuteAsString = "0" + minuteAsString;
        }
       return dayAsString + "." + monthAsString + "." + yearAsString + " " + hourAsString + ":" + minuteAsString;
    }

    public static String getDateAsString(LocalDate date, String separator) {
        if (date == null) {
            return "";
        }

        int day = date.getDayOfMonth();
        String dayAsString = String.valueOf(day);
        if (day < 10) {
            dayAsString = "0" + dayAsString;
        }

        int month = date.getMonthValue();
        String monthAsString = String.valueOf(month);
        if (month < 10) {
            monthAsString = "0" + monthAsString;
        }

        int year = date.getYear();
        String yearAsString = String.valueOf(year);

        return dayAsString + separator + monthAsString + separator + yearAsString;
    }

    public static String getShortDateAsString(LocalDate date, String separator) {
        if (date == null) {
            return "";
        }

        int day = date.getDayOfMonth();
        String dayAsString = String.valueOf(day);
        if (day < 10) {
            dayAsString = "0" + dayAsString;
        }

        int month = date.getMonthValue();
        String monthAsString = String.valueOf(month);
        if (month < 10) {
            monthAsString = "0" + monthAsString;
        }

        return dayAsString + separator + monthAsString;
    }

    public static boolean getIsWorkingDayFromWorkDay(WorkDay workDay){
        return workDay.getWorkingDayType().equals(WorkingDayType.WORKDAY);
    }

    public static String getBGDayOfWeekShort(LocalDate date) {
        return Constants.BG_WEEKDAYS_SHORT.get(date.getDayOfWeek().getValue());
    }

    public static String getBGDayOfWeekFull(LocalDate date) {
        return Constants.BG_WEEKDAYS_FULL.get(date.getDayOfWeek().getValue());
    }

    public static LocalTime getDateTimeMinusDateTimeInTime(LocalDateTime startDateTime, LocalDateTime endDateTime){
        if (startDateTime == null || endDateTime == null || endDateTime.isBefore(startDateTime)) {
            return null;
        }

        if (endDateTime.isEqual(startDateTime)) {
            return LocalTime.of(0, 0);
        }

        LocalDate startDate = startDateTime.toLocalDate();
        LocalDate endDate = endDateTime.toLocalDate();
        if (startDate.isEqual(endDate) || startDate.plusDays(1L).isEqual(endDate)) {
            LocalTime startTime = startDateTime.toLocalTime();
            LocalTime endTime = endDateTime.toLocalTime();
            return TimeCalculator.getTimeMinusTime(startTime, endTime);
        } else {
            return null;
        }
    }

    /*
    public static LocalTime calculatePaidTimeInWorkShift(EmployeeShift employeeShift, WorkDay nextDayWorkDay) {
        LocalTime paidTime = LocalTime.of(0,0);
        if (employeeShift.getStartTime() != null && employeeShift.getEndTime() != null) {
            boolean hasPaidTimeToday = false;
            boolean hasPaidTimeTomorow = false;
            if (!employeeShift.getWorkDay().getWorkingDayType().equals(WorkingDayType.WORKDAY)) {
                hasPaidTimeToday = true;
            }

            if (employeeShift.getIsEndOnNextDay() && !nextDayWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY)) {
                hasPaidTimeTomorow = true;
            }

            if (employeeShift.getSecondStartTime() != null && employeeShift.getSecondEndTime() != null) {
                //TODO
            } else {
                //TODO
            }
        }

        return paidTime;
    }
    */
}
