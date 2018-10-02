package com.officemaneger.areas.employee.services;

import com.officemaneger.areas.employee.models.bindingModels.CreateEmployeeModel;
import com.officemaneger.areas.employee.models.bindingModels.EmployeeUpdateModel;
import com.officemaneger.areas.employee.models.viewModels.*;

import java.util.List;

public interface EmployeeService {

    List<EmployeeFullNameViewModel> getNoPositionEmployees();

    List<EmployeeFullNameViewModel> getAllActiveEmployeeFullNameViewModel();

    void create(CreateEmployeeModel registerUser);

    List<EmployeeFullShortNameViewModel> getEmployeeFullShortNameViewModel();

    List<EmployeeFullAdminView> adminGetAllEmployees(boolean isOnlyActiveEmployee);

    EmployeeFullNameViewModel getEmployeeFullNameViewModel(Long id);

    boolean doWeHaveEmployeeWithOfficiaryID(String officiaryID);

    List<EmployeeViewAllModel> getEmployeeViewAllModels();

    void deleteEmployee(Long employeeId);

    EmployeeEditModel getEmployeeEditModel(Long employeeId);

    boolean doWeHaveEmployeeWithOfficiaryIDWhenUpdate(Long employeeId, String officiaryID);

    void updateEmployee(EmployeeUpdateModel employeeUpdateModel);

    List<EmployeeAnnualLeavesViewModel> getActiveEmployeeAnnualLeavesViewModels(List<String> activeAnnualLeaveTypes);

    List<Long> getActiveEmployeesIds();

    EmployeeWorkShiftViewModel getEmployeeWorkShiftViewModel(Long employeeId);

    Long getBusinessUnitIdByEmployeeId(Long employeeId);

    List<EmployeeFullNameAndOfficiaryIdViewModel> getAllActiveEmployeeFullNameAndOfficiaryIdViewModel();

    EmployeeFullNameAndOfficiaryIdViewModel getEmployeeFullNameAndOfficiaryIdViewModel(Long employeeId);

    boolean isEmployeePermanentBoss(Long employeeId);

    List<EmployeeFullShortNameViewModel> getEmployeeFullShortNameViewModels(List<Long> employeesIds);

    int getNumbOfActiveEmployees();

    int getNumbOfActiveEmployeesInBusinessUnit(Long businessUnitId);

    int getNumbOfActiveEmployeesInBusinessUnitAndSubUnit(Long businessUnitId);
}
