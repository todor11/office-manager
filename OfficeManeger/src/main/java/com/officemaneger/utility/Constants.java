package com.officemaneger.utility;

import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Constants {

    public static int DAYS_BEFOR_TODAY = 5;

    public static int DAYS_AFTER_START_DAY = 12;

    public static final LocalTime DEFAULT_SHIFT_DURATION = LocalTime.of(8, 0);

    public static final LocalTime DEFAULT_BREAK_DURATION = LocalTime.of(0, 0);

    public static final String HTML_ELEMENT_NEW_LINE = "<br/>";// "&#013";

    public static final Map<Integer, String> BG_WEEKDAYS_SHORT;

    public static final Map<Integer, String> BG_WEEKDAYS_FULL;

    public static final Map<Integer, String> BG_MONTHS;

    static {
        //set BG_WEEKDAYS_SHORT
        Map<Integer, String> weekdaysMap = new HashMap();
        weekdaysMap.put(1, "Пон");
        weekdaysMap.put(2, "Вт");
        weekdaysMap.put(3, "Ср");
        weekdaysMap.put(4, "Чет");
        weekdaysMap.put(5, "Пе");
        weekdaysMap.put(6, "Съб");
        weekdaysMap.put(7, "Нед");
        BG_WEEKDAYS_SHORT = Collections.unmodifiableMap(weekdaysMap);


        //set BG_WEEKDAYS_FULL
        Map<Integer, String> weekdaysMapFull = new HashMap();
        weekdaysMapFull.put(1, "Понеделник");
        weekdaysMapFull.put(2, "Вторник");
        weekdaysMapFull.put(3, "Сряда");
        weekdaysMapFull.put(4, "Четвъртък");
        weekdaysMapFull.put(5, "Петък");
        weekdaysMapFull.put(6, "Събота");
        weekdaysMapFull.put(7, "Неделя");
        BG_WEEKDAYS_FULL = Collections.unmodifiableMap(weekdaysMapFull);

        //set BG_MONTHS
        Map<Integer, String> montsMapFull = new HashMap();
        montsMapFull.put(1, "Януари");
        montsMapFull.put(2, "Февруари");
        montsMapFull.put(3, "Март");
        montsMapFull.put(4, "Април");
        montsMapFull.put(5, "Май");
        montsMapFull.put(6, "Юни");
        montsMapFull.put(7, "Юли");
        montsMapFull.put(8, "Август");
        montsMapFull.put(9, "Септември");
        montsMapFull.put(10, "Октомври");
        montsMapFull.put(11, "Ноември");
        montsMapFull.put(12, "Декември");
        BG_MONTHS = Collections.unmodifiableMap(montsMapFull);
    }
}
