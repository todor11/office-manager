package com.officemaneger.areas.paidHoursCalculator;

import com.officemaneger.areas.dateInterval.entities.DateInterval;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.utility.Constants;
import com.officemaneger.utility.LocalDateParser;
import com.officemaneger.utility.StringParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
public class PaidHoursController {

    private PaidHoursCalculator paidHoursCalculator;

    private UserService userService;

    public PaidHoursController() {
    }

    @GetMapping("/user/userGetHisPaidHours")
    public String getRanksPage(Model model, Principal principal){
        LocalDate today = LocalDate.now();
        LocalDate startDate = LocalDate.of(2018, 1,1);
        DateInterval dateInterval = new DateInterval(startDate, today);

        String username = principal.getName();
        Long employeeId = this.userService.getAccountOwnerIdByUsername(username);
        Map<LocalDate, LocalTime> paidHoursMap = this.paidHoursCalculator.getSingleEmployeeOverPaidHours(employeeId, dateInterval);
        List<PaidHoursEmployeeModel> paidHours = this.convertToPaidHoursEmployeeModel(paidHoursMap);
        Collections.reverse(paidHours);
        model.addAttribute("paidHours", paidHours);

        List<MonthlyPaidHoursEmployeeModel> monthlyPaidHours = this.convertToMonthlyPaidHoursEmployeeModel(paidHoursMap);
        model.addAttribute("monthlyPaidHours", monthlyPaidHours);

        model.addAttribute("title", "Платен извънреден труд");
        model.addAttribute("view", "paidHours/userGetHisPaidHours");

        return "base-layout";
    }

    @Autowired
    public void setPaidHoursCalculator(PaidHoursCalculator paidHoursCalculator) {
        this.paidHoursCalculator = paidHoursCalculator;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private List<PaidHoursEmployeeModel> convertToPaidHoursEmployeeModel(Map<LocalDate, LocalTime> paidHoursMap) {
        List<PaidHoursEmployeeModel> viewModels = new ArrayList<>();
        for (Map.Entry<LocalDate, LocalTime> dateTimeEntry : paidHoursMap.entrySet()) {
            PaidHoursEmployeeModel viewModel = new PaidHoursEmployeeModel();
            viewModel.setDate(LocalDateParser.getLocalDateAsString(dateTimeEntry.getKey(), "."));
            viewModel.setPaidHours(dateTimeEntry.getValue().getHour() + ":" + dateTimeEntry.getValue().getMinute());

            viewModels.add(viewModel);
        }

        return viewModels;
    }

    private List<MonthlyPaidHoursEmployeeModel> convertToMonthlyPaidHoursEmployeeModel(Map<LocalDate, LocalTime> paidHoursMap) {
        List<MonthlyPaidHoursEmployeeModel> viewModels = new ArrayList<>();
        Map<Integer, Map<Integer, Map<Integer, Integer[]>>> data = new LinkedHashMap<>();
        for (Map.Entry<LocalDate, LocalTime> dateTimeEntry : paidHoursMap.entrySet()) {
            Integer year = dateTimeEntry.getKey().getYear();
            Integer month = dateTimeEntry.getKey().getMonthValue();
            Integer dayOfMonth = dateTimeEntry.getKey().getDayOfMonth();
            if (!data.containsKey(year)) {
                data.put(year, new LinkedHashMap<>());
            }

            Map<Integer, Map<Integer, Integer[]>> monthsData = data.get(year);
            if (!monthsData.containsKey(month)) {
                monthsData.put(month, new LinkedHashMap<>());
            }

            Map<Integer, Integer[]> daysData = monthsData.get(month);
            if (!daysData.containsKey(dayOfMonth)) {
                daysData.put(dayOfMonth, new Integer[2]);
            }

            Integer[] dayData = daysData.get(dayOfMonth);
            if (dayData[0] == null) {
                dayData[0] = 0;
                dayData[1] = 0;
            }

            Integer allHours = dayData[0];
            Integer allMinutes = dayData[1];
            int hours = dateTimeEntry.getValue().getHour();
            int minutes = dateTimeEntry.getValue().getMinute();
            allHours += hours;
            allMinutes += minutes;
            dayData[0] = allHours;
            dayData[1] = allMinutes;
        }

        for (Map.Entry<Integer, Map<Integer, Map<Integer, Integer[]>>> integerMapMap : data.entrySet()) {
            String year = String.valueOf(integerMapMap.getKey());
            for (Map.Entry<Integer, Map<Integer, Integer[]>> integerMap : integerMapMap.getValue().entrySet()) {
                int monthAsInt = integerMap.getKey();
                String month = Constants.BG_MONTHS.get(monthAsInt);
                String dateAsString = month + " " + year;
                MonthlyPaidHoursEmployeeModel viewModel = new MonthlyPaidHoursEmployeeModel();
                int hours = 0;
                int minutes = 0;
                for (Map.Entry<Integer, Integer[]> integers : integerMap.getValue().entrySet()) {
                    Integer[] arr = integers.getValue();
                    hours = hours + arr[0];
                    minutes = minutes + arr[1];
                }

                int hoursFromMinutes = hours + (minutes / 60);
                int finalMinutes = minutes % 60;
                String finalHoursAsString = String.valueOf(hoursFromMinutes);
                if (hoursFromMinutes < 10) {
                    finalHoursAsString = "0" + finalHoursAsString;
                }

                String minutesAsString = String.valueOf(finalMinutes);
                if (finalMinutes < 10) {
                    minutesAsString = "0" + minutesAsString;
                }

                viewModel.setMonth(dateAsString);
                viewModel.setPaidHours(finalHoursAsString + ":" + minutesAsString);
                viewModels.add(viewModel);
            }
        }

        Collections.reverse(viewModels);
        return viewModels;
    }
}
