package com.officemaneger.areas.employeeShiftStatisticContainer;

import com.officemaneger.areas.employee.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeeShiftStatisticContainer {

    private Map<Long, EmployeeShiftsStatistic> employesStatistics;

    private EmployeeService employeeService;

    public EmployeeShiftStatisticContainer() {
        this.init();
    }

    public void notifyForEmployeeChanges(Long employeeId, LocalDate date, Long shiftTypeId) {
        //TODO
    }

    public void notifyForNewEmployee(Long employeeId) {

    }

    public Map<Long, EmployeeShiftsStatistic> getStatistic(List<Long> employeesIds) {
        Map<Long, EmployeeShiftsStatistic> result = new HashMap<>();
        for (Long employeeId : employeesIds) {
            EmployeeShiftsStatistic employeeShiftsStatistic = this.employesStatistics.get(employeeId);
            if (employeeShiftsStatistic == null) {
                employeeShiftsStatistic = this.getEmployeeShiftsStatistic(employeeId);
                this.employesStatistics.put(employeeId, employeeShiftsStatistic);
            }
            result.put(employeeId, employeeShiftsStatistic);
        }

        return result;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private void init() {
        this.employesStatistics = new HashMap<>();
        List<Long> activeEmployees = this.employeeService.getActiveEmployeesIds();
        for (Long employeeId : activeEmployees) {
            this.employesStatistics.put(employeeId, this.getEmployeeShiftsStatistic(employeeId));
        }
    }

    private EmployeeShiftsStatistic getEmployeeShiftsStatistic(Long employeeId) {
        //TODO
        //TODO
        //TODO
        return new EmployeeShiftsStatistic();
    }
}
