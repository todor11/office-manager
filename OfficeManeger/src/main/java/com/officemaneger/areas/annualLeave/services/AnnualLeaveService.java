package com.officemaneger.areas.annualLeave.services;

import com.officemaneger.areas.annualLeave.models.bindingModels.AnnualLeaveCreationModel;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewHisCurrent;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewHisUsed;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewModel;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveWorkShiftViewModel;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.models.viewModels.EmployeeAnnualLeavesViewModel;

import java.util.List;

public interface AnnualLeaveService {

    void create(AnnualLeaveCreationModel annualLeaveCreationModel);

    boolean dasEmployeeHasThisTypeOfAnnualLeave(Long employeeId, Long annualLeaveTypeId);

    boolean hasAnnualLeaveType(Long annualLeaveTypeId);

    AnnualLeaveEmployeeViewModel getNewAnnualLeave(Employee employee, String annualLeaveTypeFullName);

    void updateAnnualLeave(Long annualLeaveId, int numberOfAnnualLeave);

    List<AnnualLeaveWorkShiftViewModel> getAnnualLeaveWorkShiftViewModel(Long employeeId);

    void increaseAnnualLeaveToEmployee(Long employeeId, Long annualLeaveTypeId);

    void decreaseAnnualLeaveToEmployee(Long employeeId, Long annualLeaveTypeId);

    boolean hasEmployeeAnnualLeave(Long employeeId, Long annualLeaveTypeId);

    List<Long> getAnnualLeaveIdsByEmployeeIds(List<Long> employeeIds, Long annualLeaveTypeId);

    boolean hasEmployeeThisNumbOfAnnualLeave(Long employeeId, Long annualLeaveTypeId, int numbOfAnnualLeave);

    List<AnnualLeaveEmployeeViewHisCurrent> employeeGetHisCurrentAnnualLeaves(Long employeeId);

    List<AnnualLeaveEmployeeViewHisUsed> employeeGetHisUsedAnnualLeaves(Long employeeId);
}
