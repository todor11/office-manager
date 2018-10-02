package com.officemaneger.areas.employeeShift.services;

import com.officemaneger.areas.annualLeaveDateInterval.entities.AnnualLeaveDateInterval;
import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalViewModel;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employeeShift.models.bindingModels.EmployeeShiftAddMultiple;
import com.officemaneger.areas.employeeShift.models.bindingModels.EmployeeShiftInWorkShiftUpdateModel;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeFullShortNameShiftViewModel;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeShiftViewAllModel;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

public interface EmployeeShiftService {

    EmployeeShiftViewAllModel getEmployeeShiftViewAllModel(Long employeeId, LocalDate date);

    boolean updateEmployeeShiftFromWorkShift(EmployeeShiftInWorkShiftUpdateModel employeeShift);

    boolean updateOrAddMultipleEmployeeShiftsFromWorkShift(EmployeeShiftAddMultiple employeeShiftAddMultiple);

    AnnualLeaveDateIntervalViewModel updateOrAddEmployeeShiftsMultipleAnnualLeave(Employee employee, AnnualLeaveDateInterval dateInterval);

    List<EmployeeFullShortNameShiftViewModel> getEmployeeInfoByDateAndShiftTypeAndBusinessUnit(LocalDate date, Long shiftTypeId, Long businessUnitId);

    List<EmployeeFullShortNameShiftViewModel> getEmployeeInfoByDateAndShiftTypeByBusinessUnitAndSubUnit(LocalDate date, Long shiftTypeId, Long businessUnitId);

    Map<LocalDate,LocalTime> getSingleEmployeeOverPaidHours(Long employeeId, List<LocalDate> restDays);
}
