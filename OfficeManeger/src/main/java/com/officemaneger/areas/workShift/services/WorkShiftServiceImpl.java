package com.officemaneger.areas.workShift.services;

import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.computers.services.ComputerService;
import com.officemaneger.areas.employee.models.viewModels.EmployeeWorkShiftViewModel;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeShiftViewAllModel;
import com.officemaneger.areas.employeeShift.services.EmployeeShiftService;
import com.officemaneger.areas.workShift.models.viewModels.WorkShiftViewAllModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WorkShiftServiceImpl implements WorkShiftService {

    private EmployeeService employeeService;

    private EmployeeShiftService employeeShiftService;

    private BusinessUnitService businessUnitService;

    private ComputerService computerService;

    public WorkShiftServiceImpl() {
    }

    @Override
    public List<WorkShiftViewAllModel> getWorkShiftViewAllModels(List<LocalDate> dates) {
        List<Long> activeEmployeeIds = this.employeeService.getActiveEmployeesIds();
        return this.getWorkShiftViewAllModelsByDatesAndEmployee(dates, activeEmployeeIds);
    }

    @Override
    public List<WorkShiftViewAllModel> getWorkShiftViewAllModelsByBossId(List<LocalDate> dates, Long bossId) {
        Long businessUnitId = this.employeeService.getBusinessUnitIdByEmployeeId(bossId);
        if (businessUnitId == null) {
            return null;
        }

        List<Long> businessUnitEmployeesIds = this.businessUnitService.getBusinessUnitEmployeesIds(businessUnitId);

        return this.getWorkShiftViewAllModelsByDatesAndEmployee(dates, businessUnitEmployeesIds);
    }

    @Override
    public List<WorkShiftViewAllModel> getWorkShiftViewAllModelsByComputerId(List<LocalDate> dates, Long computerId) {
        Long businessUnitId = this.computerService.getBusinessUnitIdByComputerId(computerId);
        if (businessUnitId == null) {
            return null;
        }

        List<Long> businessUnitEmployeesIds = this.businessUnitService.getBusinessUnitEmployeesIds(businessUnitId);

        return this.getWorkShiftViewAllModelsByDatesAndEmployee(dates, businessUnitEmployeesIds);
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setEmployeeShiftService(EmployeeShiftService employeeShiftService) {
        this.employeeShiftService = employeeShiftService;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @Autowired
    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    private List<WorkShiftViewAllModel> getWorkShiftViewAllModelsByDatesAndEmployee(List<LocalDate> dates, List<Long> employeeIds) {
        List<WorkShiftViewAllModel> viewAllModels = new ArrayList<>();
        for (Long employeeId : employeeIds) {
            WorkShiftViewAllModel viewModel = new WorkShiftViewAllModel();
            EmployeeWorkShiftViewModel employee = this.employeeService.getEmployeeWorkShiftViewModel(employeeId);
            viewModel.setEmployee(employee);
            List<EmployeeShiftViewAllModel> employeeShifts = new ArrayList<>();
            for (LocalDate date : dates) {
                employeeShifts.add(this.employeeShiftService.getEmployeeShiftViewAllModel(employeeId, date));
            }
            viewModel.setEmployeeShifts(employeeShifts);

            viewAllModels.add(viewModel);
        }

        return viewAllModels;
    }
}
