package com.officemaneger.areas.annualLeave.services;

import com.officemaneger.areas.annualLeave.entities.AnnualLeave;
import com.officemaneger.areas.annualLeave.models.bindingModels.AnnualLeaveCreationModel;
import com.officemaneger.areas.annualLeave.models.interfaces.AnnualLeaveWorkShiftView;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewHisCurrent;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewHisUsed;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewModel;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveWorkShiftViewModel;
import com.officemaneger.areas.annualLeave.repositories.AnnualLeaveRepository;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.annualLeaveType.repositories.AnnualLeaveTypeRepository;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.repositories.EmployeeRepository;
import com.officemaneger.areas.employeeShift.models.interfaces.EmployeeShiftTypeAndDate;
import com.officemaneger.areas.employeeShift.repositories.EmployeeShiftRepository;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.utility.DateCalculator;
import com.officemaneger.utility.StringParser;
import com.officemaneger.utility.TimeCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnnualLeaveServiceImpl implements AnnualLeaveService {

    private AnnualLeaveRepository annualLeaveRepository;

    private EmployeeRepository employeeRepository;

    private AnnualLeaveTypeRepository annualLeaveTypeRepository;

    private ModelMapper modelMapper;

    private EmployeeShiftRepository employeeShiftRepository;

    private WorkDayService workDayService;

    public AnnualLeaveServiceImpl() {
    }

    @Override
    public void create(AnnualLeaveCreationModel annualLeaveCreationModel) {
        AnnualLeave annualLeave = new AnnualLeave();
        Employee employee = this.employeeRepository.findOne(annualLeaveCreationModel.getEmployeeId());
        annualLeave.setOwner(employee);
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOne(annualLeaveCreationModel.getAnnualLeaveTypeId());
        annualLeave.setType(annualLeaveType);
        // check for pre
        annualLeave.setNumberOfAnnualLeave(annualLeaveCreationModel.getNumberOfAnnualLeave());

        this.annualLeaveRepository.save(annualLeave);
    }

    @Override
    public boolean dasEmployeeHasThisTypeOfAnnualLeave(Long employeeId, Long annualLeaveTypeId) {
        Long annualLeaveId = this.annualLeaveRepository.getAnnualLeaveIdByEmployeeIdAndTypeId(employeeId, annualLeaveTypeId);
        if (annualLeaveId == null) {
            return false;
        }
        return true;
    }

    @Override
    public boolean hasEmployeeAnnualLeave(Long employeeId, Long annualLeaveTypeId) {
        int numbOfAnnualLeave = this.annualLeaveRepository.getNumbOfAnnualLeaveByEmployeeIdAndAnnualLeaveTypeId(employeeId, annualLeaveTypeId);
        if (numbOfAnnualLeave > 0) {
            return true;
        }

        return false;
    }

    @Override
    public boolean hasAnnualLeaveType(Long annualLeaveTypeId) {
        Long numbOfAnnualLeave = this.annualLeaveRepository.countByAnnualLeaveType(annualLeaveTypeId);
        if (numbOfAnnualLeave == 0) {
            return false;
        }

        return true;
    }

    @Override
    public AnnualLeaveEmployeeViewModel getNewAnnualLeave(Employee employee, String annualLeaveTypeFullName) {
        AnnualLeave annualLeave = new AnnualLeave();
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOneByFullName(annualLeaveTypeFullName);
        annualLeave.setType(annualLeaveType);
        annualLeave.setNumberOfAnnualLeave(0);
        AnnualLeave savedAnnualLeave = this.annualLeaveRepository.save(annualLeave);
        savedAnnualLeave.setOwner(employee);

        AnnualLeaveEmployeeViewModel viewModel = new AnnualLeaveEmployeeViewModel();
        viewModel.setId(savedAnnualLeave.getId());
        viewModel.setNumberOfAnnualLeave(savedAnnualLeave.getNumberOfAnnualLeave());

        return viewModel;
    }

    @Override
    public void updateAnnualLeave(Long annualLeaveId, int numberOfAnnualLeave) {
        AnnualLeave annualLeave = this.annualLeaveRepository.findOne(annualLeaveId);
        if (annualLeave == null){
            return;
        }
        annualLeave.setNumberOfAnnualLeave(numberOfAnnualLeave);
    }

    @Override
    public List<AnnualLeaveWorkShiftViewModel> getAnnualLeaveWorkShiftViewModel(Long employeeId) {
        List<AnnualLeaveWorkShiftView> annualLeaveWorkShiftViews = this.annualLeaveRepository.getAnnualLeaveWorkShiftViewByEmployeeIdOrdered(employeeId);
        List<AnnualLeaveWorkShiftViewModel> viewModels = new ArrayList<>();
        for (AnnualLeaveWorkShiftView annualLeaveWorkShiftView : annualLeaveWorkShiftViews) {
            viewModels.add(this.modelMapper.map(annualLeaveWorkShiftView, AnnualLeaveWorkShiftViewModel.class));
        }

        return viewModels;
    }

    @Override
    public void increaseAnnualLeaveToEmployee(Long employeeId, Long annualLeaveTypeId) {
        Long annualLeaveId = this.annualLeaveRepository.getAnnualLeaveIdByEmployeeIdAndTypeId(employeeId, annualLeaveTypeId);
        if (annualLeaveId == null) {
            return;
        }
        int oldNumberOfAnnualLeave = this.annualLeaveRepository.getNumbOfAnnualLeaveByEmployeeIdAndAnnualLeaveTypeId(employeeId, annualLeaveTypeId);
        int newNumberOfAnnualLeave =  oldNumberOfAnnualLeave + 1;
        this.annualLeaveRepository.setNumberOfAnnualLeave(annualLeaveId, newNumberOfAnnualLeave);
    }

    @Override
    public void decreaseAnnualLeaveToEmployee(Long employeeId, Long annualLeaveTypeId) {
        Long annualLeaveId = this.annualLeaveRepository.getAnnualLeaveIdByEmployeeIdAndTypeId(employeeId, annualLeaveTypeId);
        if (annualLeaveId == null) {
            return;
        }
        int oldNumberOfAnnualLeave = this.annualLeaveRepository.getNumbOfAnnualLeaveByEmployeeIdAndAnnualLeaveTypeId(employeeId, annualLeaveTypeId);
        int newNumberOfAnnualLeave =  oldNumberOfAnnualLeave - 1;
        if (newNumberOfAnnualLeave < 0) {
            newNumberOfAnnualLeave = 0;
        }
        this.annualLeaveRepository.setNumberOfAnnualLeave(annualLeaveId, newNumberOfAnnualLeave);
    }

    @Override
    public List<Long> getAnnualLeaveIdsByEmployeeIds(List<Long> employeeIds, Long annualLeaveTypeId) {
        List<Long> annualLeaveIds = new ArrayList<>();
        for (Long employeeId : employeeIds) {
            Long annualLeaveId = this.annualLeaveRepository.getAnnualLeaveIdByEmployeeIdAndTypeId(employeeId, annualLeaveTypeId);
            if (annualLeaveId == null) {
                AnnualLeave newAnnualLeave = new AnnualLeave();
                AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOne(annualLeaveTypeId);
                Employee employee = this.employeeRepository.findOne(employeeId);
                newAnnualLeave.setType(annualLeaveType);
                newAnnualLeave.setNumberOfAnnualLeave(0);
                AnnualLeave savedAnnualLeave = this.annualLeaveRepository.save(newAnnualLeave);
                savedAnnualLeave.setOwner(employee);
                annualLeaveId = savedAnnualLeave.getId();
            }
            annualLeaveIds.add(annualLeaveId);
        }

        return annualLeaveIds;
    }

    @Override
    public boolean hasEmployeeThisNumbOfAnnualLeave(Long employeeId, Long annualLeaveTypeId, int numbOfAnnualLeave) {
        int numbFromDb = this.annualLeaveRepository.getNumbOfAnnualLeaveByEmployeeIdAndAnnualLeaveTypeId(employeeId, annualLeaveTypeId);
        if (numbFromDb == 0 || numbFromDb < numbOfAnnualLeave) {
            return false;
        }

        return true;
    }

    @Override
    public List<AnnualLeaveEmployeeViewHisCurrent> employeeGetHisCurrentAnnualLeaves(Long employeeId) {
        List<AnnualLeaveEmployeeViewHisCurrent> result = new ArrayList<>();
        List<AnnualLeave> annualLeaves = this.annualLeaveRepository.getAnnualLeavesByEmployeeId(employeeId);
        for (AnnualLeave annualLeave : annualLeaves) {
            AnnualLeaveEmployeeViewHisCurrent currentAnnualLeaveView = new AnnualLeaveEmployeeViewHisCurrent();
            currentAnnualLeaveView.setType(annualLeave.getType().getFullName());
            currentAnnualLeaveView.setNumbOfDays(annualLeave.getNumberOfAnnualLeave());
            result.add(currentAnnualLeaveView);
        }

        return result;
    }

    @Override
    public List<AnnualLeaveEmployeeViewHisUsed> employeeGetHisUsedAnnualLeaves(Long employeeId) {
        List<Long> allAnnualLeaveTypeIds = this.annualLeaveTypeRepository.getAllAnnualLeaveTypeIds();
        List<EmployeeShiftTypeAndDate> typeAndDates = this.employeeShiftRepository.getShiftTypeAndDatesByEmployeeIdAndTypeOrderedByDateDesc(employeeId, allAnnualLeaveTypeIds);
        LocalDate startDate = null;
        LocalDate endDate = null;
        LocalDate previousDate = null;
        String currentShiftType = null;
        AnnualLeaveEmployeeViewHisUsed currentInterval = new AnnualLeaveEmployeeViewHisUsed();
        List<AnnualLeaveEmployeeViewHisUsed> result = new ArrayList<>();
        int typeAndDatesSize = typeAndDates.size();
        if (typeAndDatesSize > 0) {
            currentShiftType = typeAndDates.get(0).getShiftType();
            startDate = typeAndDates.get(0).getDate();
            currentInterval.setType(currentShiftType);
            currentInterval.setEndDate(TimeCalculator.getDateAsString(startDate, "."));
            currentInterval.setNumbOfDays(1);
            previousDate = startDate;
        }

        for (int i = 1; i < typeAndDatesSize; i++) {
            EmployeeShiftTypeAndDate typeAndDate = typeAndDates.get(i);
            if (previousDate.minusDays(1L).equals(typeAndDate.getDate())) {
                if (currentInterval.getType().equals(typeAndDate.getShiftType())) {
                    currentInterval.setNumbOfDays(currentInterval.getNumbOfDays() + 1);
                    endDate = typeAndDate.getDate();
                    previousDate = typeAndDate.getDate();
                } else {
                    currentInterval.setStartDate(TimeCalculator.getDateAsString(endDate, "."));
                    result.add(currentInterval);
                    currentInterval = new AnnualLeaveEmployeeViewHisUsed();
                    startDate = typeAndDate.getDate();
                    currentInterval.setEndDate(TimeCalculator.getDateAsString(startDate, "."));
                    currentInterval.setType(typeAndDate.getShiftType());
                    currentInterval.setNumbOfDays(1);
                    previousDate = startDate;
                    endDate = previousDate;
                }
            } else {
                LocalDate tempDate = previousDate.minusDays(1L);
                boolean isNewPeriod = false;
                if (!currentInterval.getType().equals(typeAndDate.getShiftType())) {
                    isNewPeriod = true;
                }

                if (!isNewPeriod) {
                    while (typeAndDate.getDate().isBefore(tempDate)) {
                        if (this.workDayService.isDayWorkingDay(tempDate)) {
                            isNewPeriod = true;
                            break;
                        }

                        tempDate = tempDate.minusDays(1L);
                    }
                }

                if (isNewPeriod) {
                    currentInterval.setStartDate(TimeCalculator.getDateAsString(endDate, "."));
                    result.add(currentInterval);
                    currentInterval = new AnnualLeaveEmployeeViewHisUsed();
                    startDate = typeAndDate.getDate();
                    currentInterval.setEndDate(TimeCalculator.getDateAsString(startDate, "."));
                    currentInterval.setType(typeAndDate.getShiftType());
                    currentInterval.setNumbOfDays(1);
                    previousDate = startDate;
                    endDate = previousDate;
                } else {
                    currentInterval.setNumbOfDays(currentInterval.getNumbOfDays() + 1);
                    endDate = typeAndDate.getDate();
                    previousDate = typeAndDate.getDate();
                }
            }
        }

        if (typeAndDatesSize > 0) {
            currentInterval.setStartDate(TimeCalculator.getDateAsString(previousDate, "."));
            result.add(currentInterval);
        }

        return result;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    @Autowired
    public void setAnnualLeaveRepository(AnnualLeaveRepository annualLeaveRepository) {
        this.annualLeaveRepository = annualLeaveRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setAnnualLeaveTypeRepository(AnnualLeaveTypeRepository annualLeaveTypeRepository) {
        this.annualLeaveTypeRepository = annualLeaveTypeRepository;
    }

    @Autowired
    public void setEmployeeShiftRepository(EmployeeShiftRepository employeeShiftRepository) {
        this.employeeShiftRepository = employeeShiftRepository;
    }
}
