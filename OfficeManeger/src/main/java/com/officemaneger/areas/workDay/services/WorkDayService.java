package com.officemaneger.areas.workDay.services;

import com.officemaneger.areas.dateInterval.entities.DateInterval;
import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.areas.workDay.models.bindingModels.AddHolidayModel;
import com.officemaneger.areas.workDay.models.bindingModels.AddWorkDayModel;
import com.officemaneger.areas.workDay.models.bindingModels.DeleteWorkDayBindingModel;
import com.officemaneger.areas.workDay.models.viewModels.HolidayViewModel;
import com.officemaneger.areas.workDay.models.viewModels.WorkDayViewModel;

import java.time.LocalDate;
import java.util.List;

public interface WorkDayService {

    WorkDayViewModel create(AddWorkDayModel addWorkDayModel);

    HolidayViewModel create(AddHolidayModel addHolidayModel);

    List<WorkDayViewModel> getAllExtraWorkingDays();

    List<HolidayViewModel> getAllHolidayDays();

    void delete(Long id);

    WorkDay getWorkDayByDate(LocalDate date);

    List<LocalDate> getWorkDaysFromDateToDate(LocalDate startDate, LocalDate endDate);

    Long getNumbOfWorkingDaysBetweenDates(LocalDate startDate, LocalDate endDate);

    boolean isDayWorkingDay(LocalDate date);

    List<LocalDate> getRestDaysFromInterval(DateInterval dateInterval);
}
