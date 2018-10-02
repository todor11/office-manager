package com.officemaneger.areas.workDay.services;

import com.officemaneger.areas.dateInterval.entities.DateInterval;
import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.enums.WorkingDayType;
import com.officemaneger.areas.workDay.models.bindingModels.AddHolidayModel;
import com.officemaneger.areas.workDay.models.bindingModels.AddWorkDayModel;
import com.officemaneger.areas.workDay.models.viewModels.HolidayViewModel;
import com.officemaneger.areas.workDay.models.viewModels.WorkDayViewModel;
import com.officemaneger.areas.workDay.repositories.WorkDayRepository;
import com.officemaneger.utility.DateCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WorkDayServiceImpl implements WorkDayService {

    private WorkDayRepository workDayRepository;

    private ModelMapper modelMapper;

    public WorkDayServiceImpl() {
    }

    @Override
    public WorkDayViewModel create(AddWorkDayModel addWorkDayModel) {
        addWorkDayModel.setIsRestDayOfWeek();
        WorkDay workDay = this.modelMapper.map(addWorkDayModel, WorkDay.class);
        WorkDay savedWorkDay = workDayRepository.save(workDay);

        return this.modelMapper.map(savedWorkDay, WorkDayViewModel.class);
    }

    @Override
    public HolidayViewModel create(AddHolidayModel addHolidayModel) {
        addHolidayModel.setIsRestDayOfWeek();
        WorkDay workDay = this.modelMapper.map(addHolidayModel, WorkDay.class);
        WorkDay savedHoliday = this.workDayRepository.save(workDay);
        return this.modelMapper.map(savedHoliday, HolidayViewModel.class);
    }

    @Override
    public WorkDay getWorkDayByDate(LocalDate date) {
        WorkDay workDayDB = this.workDayRepository.getWorkDayByDate(date);
        if (workDayDB != null) {
            return workDayDB;
        }

        return this.createWorkDay(date);
    }

    @Override
    public List<LocalDate> getWorkDaysFromDateToDate(LocalDate startDate, LocalDate endDate) {
        Boolean isValid = this.checkHasWorkDaysAndCreateIfNecessary(startDate, endDate);
        if (isValid == null) {
            return null;
        }

        return this.workDayRepository.getDatesFromDateToDateByType(startDate, endDate, WorkingDayType.WORKDAY);
    }

    @Override
    public Long getNumbOfWorkingDaysBetweenDates(LocalDate startDate, LocalDate endDate) {
        Boolean isValid = this.checkHasWorkDaysAndCreateIfNecessary(startDate, endDate);
        if (isValid == null) {
            return null;
        }

        return this.workDayRepository.getNumbOfWorkDaysBetweenDatesByType(startDate, endDate, WorkingDayType.WORKDAY);
    }

    @Override
    public boolean isDayWorkingDay(LocalDate date) {
        WorkDay workDay = this.workDayRepository.getWorkDayByDate(date);
        if (workDay == null) {
            workDay = this.createWorkDay(date);
        }

        return workDay.getWorkingDayType().equals(WorkingDayType.WORKDAY);
    }

    @Override
    public List<WorkDayViewModel> getAllExtraWorkingDays() {
        List<WorkDay> workDays = this.workDayRepository.findAllByWorkingDayTypeAndIsRestDayOfWeek(WorkingDayType.WORKDAY, true);
        List<WorkDayViewModel> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (WorkDay workDay : workDays) {
            WorkDayViewModel workDayViewModel = new WorkDayViewModel();
            modelMapper.map(workDay, workDayViewModel);
            result.add(workDayViewModel);
        }

        return result;
    }

    @Override
    public List<HolidayViewModel> getAllHolidayDays() {
        List<WorkDay> holidays = this.workDayRepository.findAllByWorkingDayType(WorkingDayType.HOLIDAY);
        List<HolidayViewModel> result = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();
        for (WorkDay workDay : holidays) {
            HolidayViewModel holidayViewModel = new HolidayViewModel();
            modelMapper.map(workDay, holidayViewModel);
            result.add(holidayViewModel);
        }

        return result;
    }

    @Override
    public void delete(Long id) {
        this.workDayRepository.delete(id);
    }

    @Override
    public List<LocalDate> getRestDaysFromInterval(DateInterval dateInterval) {
        List<LocalDate> restDays = new ArrayList<>();
        LocalDate tempDate = dateInterval.getStartDate();
        LocalDate endDate = dateInterval.getEndDate();
        if (tempDate == null || endDate == null) {
            return null;
        }

        while (!tempDate.isAfter(endDate)) {
            WorkDay currentWorkDay = this.workDayRepository.getWorkDayByDate(tempDate);
            if (!currentWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY)) {
                restDays.add(tempDate);
            }

            tempDate = tempDate.plusDays(1L);
        }

        return restDays;
    }

    @Autowired
    public void setWorkDayRepository(WorkDayRepository workDayRepository) {
        this.workDayRepository = workDayRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private Boolean checkHasWorkDaysAndCreateIfNecessary(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            LocalDate tempDate = startDate;
            startDate = endDate;
            endDate = tempDate;
        }

        Long numbOfDaysToCheck = DateCalculator.getNumbOfDaysBetweenDatesIncludingThem(startDate, endDate);
        if (numbOfDaysToCheck == null) {
            return null;
        }

        Long numbOfWorkDaysInDB = this.workDayRepository.getNumbOfWorkDaysBetweenDates(startDate, endDate);
        if (!numbOfDaysToCheck.equals(numbOfWorkDaysInDB)) {
            while (!startDate.isAfter(endDate)) {
                Long workDayId = this.workDayRepository.getWorkDayIdByDate(startDate);
                if (workDayId == null) {
                    //create new one
                    this.createWorkDay(startDate);
                }

                startDate = startDate.plusDays(1L);
            }
        }

        return true;
    }

    private WorkDay createWorkDay(LocalDate date) {
        WorkDay workDay = new WorkDay();
        workDay.setDate(date);
        if (date.getDayOfWeek().getValue() == 7 || date.getDayOfWeek().getValue() == 6) {
            workDay.setIsRestDayOfWeek(true);
            workDay.setWorkingDayType(WorkingDayType.RESTDAY);
        } else {
            workDay.setIsRestDayOfWeek(false);
            workDay.setWorkingDayType(WorkingDayType.WORKDAY);
        }

        return this.workDayRepository.save(workDay);
    }
}
