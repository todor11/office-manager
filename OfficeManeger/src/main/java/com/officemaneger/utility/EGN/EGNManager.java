package com.officemaneger.utility.EGN;

import com.officemaneger.enums.Gender;

import java.time.LocalDate;
import java.util.*;

public class EGNManager {

    private static final String EGN_NOT_VALID = "ЕГН-то не е валидно";

    private static final List<Integer> INDEX_WEIGHT;

    private static final Map<String, Integer> REGIONS;

    private static final String START_REGEX = "^\\d{10}$";

    static {
        // set index weight
        List<Integer> indexWeight = new ArrayList<>();
        indexWeight.add(2);
        indexWeight.add(4);
        indexWeight.add(8);
        indexWeight.add(5);
        indexWeight.add(10);
        indexWeight.add(9);
        indexWeight.add(7);
        indexWeight.add(3);
        indexWeight.add(6);
        INDEX_WEIGHT = Collections.unmodifiableList(indexWeight);

        //set regions
        Map<String, Integer> regions = new HashMap<>();
        regions.put("Благоевград", 0);
        regions.put("Бургас", 44);
        regions.put("Варна", 94);
        regions.put("Велико Търново", 140);
        regions.put("Видин", 170);
        regions.put("Враца", 184);
        regions.put("Габрово", 218);
        regions.put("Кърджали", 234);
        regions.put("Кюстендил", 282);
        regions.put("Ловеч", 302);
        regions.put("Монтана", 320);
        regions.put("Пазарджик", 342);
        regions.put("Перник", 378);
        regions.put("Плевен", 396);
        regions.put("Пловдив", 436);
        regions.put("Разград", 502);
        regions.put("Русе", 528);
        regions.put("Силистра", 556);
        regions.put("Сливен", 576);
        regions.put("Смолян", 602);
        regions.put("София - град", 624);
        regions.put("София - окръг", 722);
        regions.put("Стара Загора", 752);
        regions.put("Добрич", 790);
        regions.put("Търговище", 822);
        regions.put("Хасково", 844);
        regions.put("Шумен", 872);
        regions.put("Ямбол", 904);
        regions.put("Друг/Неизвестен", 926);
        REGIONS = Collections.unmodifiableMap(regions);
    }

    public static boolean isValidEGN(String egn) {
        if (!egn.matches(EGNManager.START_REGEX)) {
            return false;
        }

        int sum = 0;
        for (int i = 0; i < 9; i++) {
            int digit = Integer.valueOf(egn.substring(i, i + 1));
            sum += digit * EGNManager.INDEX_WEIGHT.get(i);
        }

        int lastDigit = Integer.valueOf(egn.substring(9));
        int resultDigit = sum % 11;
        if (resultDigit > 9) {
            resultDigit = 0;
        }

        if (lastDigit != resultDigit) {
            return false;
        }

        LocalDate date = EGNManager.getDateOfBirthValidEGN(egn);
        if (date == null) {
            return false;
        }

        return true;
    }

    public static Gender getGender(String egn) {
        if (!EGNManager.isValidEGN(egn)) {
            return null;
        }

        int digit = Integer.valueOf(egn.substring(8, 9));
        if ((digit & 1) == 1) {
            return Gender.FEMALE;
        } else {
            return Gender.MALE;
        }
    }

    public static String getRegionOfBirth(String egn) {
        /* Отделени номера */
        //EGN_REGIONS["Благоевград"]       = 43;  /* от 000 до 043 */
        //EGN_REGIONS["Бургас"]            = 93;  /* от 044 до 093 */
        //EGN_REGIONS["Варна"]             = 139; /* от 094 до 139 */
        //EGN_REGIONS["Велико Търново"]    = 169; /* от 140 до 169 */
        //EGN_REGIONS["Видин"]             = 183; /* от 170 до 183 */
        //EGN_REGIONS["Враца"]             = 217; /* от 184 до 217 */
        //EGN_REGIONS["Габрово"]           = 233; /* от 218 до 233 */
        //EGN_REGIONS["Кърджали"]          = 281; /* от 234 до 281 */
        //EGN_REGIONS["Кюстендил"]         = 301; /* от 282 до 301 */
        //EGN_REGIONS["Ловеч"]             = 319; /* от 302 до 319 */
        //EGN_REGIONS["Монтана"]           = 341; /* от 320 до 341 */
        //EGN_REGIONS["Пазарджик"]         = 377; /* от 342 до 377 */
        //EGN_REGIONS["Перник"]            = 395; /* от 378 до 395 */
        //EGN_REGIONS["Плевен"]            = 435; /* от 396 до 435 */
        //EGN_REGIONS["Пловдив"]           = 501; /* от 436 до 501 */
        //EGN_REGIONS["Разград"]           = 527; /* от 502 до 527 */
        //EGN_REGIONS["Русе"]              = 555; /* от 528 до 555 */
        //EGN_REGIONS["Силистра"]          = 575; /* от 556 до 575 */
        //EGN_REGIONS["Сливен"]            = 601; /* от 576 до 601 */
        //EGN_REGIONS["Смолян"]            = 623; /* от 602 до 623 */
        //EGN_REGIONS["София - град"]      = 721; /* от 624 до 721 */
        //EGN_REGIONS["София - окръг"]     = 751; /* от 722 до 751 */
        //EGN_REGIONS["Стара Загора"]      = 789; /* от 752 до 789 */
        //EGN_REGIONS["Добрич (Толбухин)"] = 821; /* от 790 до 821 */
        //EGN_REGIONS["Търговище"]         = 843; /* от 822 до 843 */
        //EGN_REGIONS["Хасково"]           = 871; /* от 844 до 871 */
        //EGN_REGIONS["Шумен"]             = 903; /* от 872 до 903 */
        //EGN_REGIONS["Ямбол"]             = 925; /* от 904 до 925 */
        //EGN_REGIONS["Друг/Неизвестен"]   = 999; /* от 926 до 999 - Такъв регион понякога се ползва при
        //родени преди 1900, за родени в чужбина
        //или ако в даден регион се родят повече
        //деца от предвиденото. Доколкото ми е
        //известно няма правило при ползването
        //на 926 - 999 */

        if (!EGNManager.isValidEGN(egn)) {
            return null;
        }
        
        int digit = Integer.valueOf(egn.substring(6, 9));
        if (digit > 501) {
            if (digit > 751) {
                if (digit > 843) {
                    if (digit > 903) {
                        if (digit > 925) {
                            return "Друг/Неизвестен";
                        } else {
                            return "Ямбол";
                        }
                    } else {
                        if (digit > 871) {
                            return "Шумен";
                        } else {
                            return "Хасково";
                        }
                    }
                } else {
                    if (digit > 789) {
                        if (digit > 821) {
                            return "Търговище";
                        } else {
                            return "Добрич";
                        }
                    } else {
                        return "Стара Загора";
                    }
                }
            } else {
                if (digit > 575) {
                    if (digit > 623) {
                        if (digit > 721) {
                            return "София - окръг";
                        } else {
                            return "София - град";
                        }
                    } else {
                        if (digit > 601) {
                            return "Смолян";
                        } else {
                            return "Сливен";
                        }
                    }
                } else {
                    if (digit > 555) {
                        return "Силистра";
                    } else {
                        if (digit > 527) {
                            return "Русе";
                        } else {
                            return "Разград";
                        }
                    }
                }
            }
        } else {
            if (digit > 233) {
                if (digit > 341) {
                    if (digit > 395) {
                        if (digit > 435) {
                            return "Пловдив";
                        } else {
                            return "Плевен";
                        }
                    } else {
                        if (digit > 377) {
                            return "Перник";
                        } else {
                            return "Пазарджик";
                        }
                    }
                } else {
                    if (digit > 301) {
                        if (digit > 319) {
                            return "Монтана";
                        } else {
                            return "Ловеч";
                        }
                    } else {
                        if (digit > 281) {
                            return "Кюстендил";
                        } else {
                            return "Кърджали";
                        }
                    }
                }
            } else {
                if (digit > 169) {
                    if (digit > 183) {
                        if (digit > 217) {
                            return "Габрово";
                        } else {
                            return "Враца";
                        }
                    } else {
                        return "Видин";
                    }
                } else {
                    if (digit > 93) {
                        if (digit > 139) {
                            return "Велико Търново";
                        } else {
                            return "Варна";
                        }
                    } else {
                        if (digit > 43) {
                            return "Бургас";
                        } else {
                            return "Благоевград";
                        }
                    }
                }
            }
        }
    }

    public static String getNumbOfChildrenBeforeHim(String egn) {
        String region = EGNManager.getRegionOfBirth(egn);
        if (region == null) {
            return null;
        }

        int startDigit = EGNManager.REGIONS.get(region);
        int digit = Integer.valueOf(egn.substring(6, 9));
        int allNumb = digit - startDigit;
        int resultNumb = allNumb / 2;

        if ((Integer.valueOf(egn.substring(8, 9)) & 1) == 1) {
            return "Преди нея в този ден и регион са се родили " + resultNumb + " момичета";
        } else {
            return "Преди него в този ден и регион са се родили " + resultNumb + " момчета";
        }
    }

    public static LocalDate getDateOfBirth(String egn) {
        if (!EGNManager.isValidEGN(egn)) {
            return null;
        }

        return EGNManager.getDateOfBirthValidEGN(egn);
    }

    public static String getDateOfBirthAsString(String egn) {
        LocalDate date = EGNManager.getDateOfBirth(egn);
        if (date == null) {
            return null;
        }

        int day = date.getDayOfMonth();
        String dayAsString = String.valueOf(day);
        if (day < 10) {
            dayAsString = '0' + dayAsString;
        }

        int month = date.getMonthValue();
        String monthAsString = String.valueOf(month);
        if (month < 10) {
            monthAsString = '0' + monthAsString;
        }

        int year = date.getYear();
        String yearAsString = String.valueOf(year);
        return dayAsString + '.' + monthAsString + '.' + yearAsString;
    }

    public static String getBirthDay(String egn) {
        LocalDate date = EGNManager.getDateOfBirth(egn);
        if (date == null) {
            return null;
        }

        int day = date.getDayOfMonth();
        String dayAsString = String.valueOf(day);
        if (day < 10) {
            dayAsString = '0' + dayAsString;
        }

        int month = date.getMonthValue();
        String monthAsString = String.valueOf(month);
        if (month < 10) {
            monthAsString = '0' + monthAsString;
        }

        return dayAsString + '.' + monthAsString;
    }

    private static LocalDate getDateOfBirthValidEGN(String egn) {
        int year, month, day;

        String yearAsString = egn.substring(0,2);
        String monthAsString = egn.substring(2,4);
        String dayAsString = egn.substring(4,6);

        if (Integer.valueOf(monthAsString) > 40) {
            year = 2000 + Integer.valueOf(yearAsString);
            month = Integer.valueOf(monthAsString) - 40;
        } else if (Integer.valueOf(monthAsString) > 20){
            year = 1800 + Integer.valueOf(yearAsString);
            month = Integer.valueOf(monthAsString);
        } else {
            year = 1900 + Integer.valueOf(yearAsString);
            month = Integer.valueOf(monthAsString);
        }

        day = Integer.valueOf(dayAsString);
        if (year < 1800 || month < 1 || month > 12 || day < 1 || day > 31) {
            return null;
        }

        return LocalDate.of(year, month, day);
    }
}
