package com.officemaneger.areas.paidHoursCalculator;

import com.officemaneger.areas.dateInterval.entities.DateInterval;
import com.officemaneger.areas.employeeShift.services.EmployeeShiftService;
import com.officemaneger.areas.workDay.services.WorkDayService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public class PaidHoursCalculator {

    private WorkDayService workDayService;

    private EmployeeShiftService employeeShiftService;

    public PaidHoursCalculator() {
    }

    public Map<LocalDate, LocalTime> getSingleEmployeeOverPaidHours(Long employeeId, DateInterval dateInterval) {
        List<LocalDate> restDays = this.workDayService.getRestDaysFromInterval(dateInterval);
        Map<LocalDate, LocalTime> paidHoursMap = this.employeeShiftService.getSingleEmployeeOverPaidHours(employeeId, restDays);

        return paidHoursMap;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    @Autowired
    public void setEmployeeShiftService(EmployeeShiftService employeeShiftService) {
        this.employeeShiftService = employeeShiftService;
    }
}
