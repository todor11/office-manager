package com.officemaneger.areas.workSchedule.services;

import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeFullShortNameShiftViewModel;
import com.officemaneger.areas.employeeShift.services.EmployeeShiftService;
import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeViewModel;
import com.officemaneger.areas.shiftType.services.ShiftTypeService;
import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupScheduleViewModel;
import com.officemaneger.areas.shiftTypeGroup.services.ShiftTypeGroupService;
import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.areas.workSchedule.models.viewModels.WorkScheduleViewAllModel;
import com.officemaneger.areas.workSchedule.models.viewModels.WorkShiftByGroupAndDateViewModel;
import com.officemaneger.areas.workScheduleSettings.entities.WorkScheduleSettings;
import com.officemaneger.areas.workScheduleSettings.repositories.WorkScheduleSettingsRepository;
import com.officemaneger.areas.workScheduleSettings.services.WorkScheduleSettingsService;
import com.officemaneger.enums.WorkingDayType;
import com.officemaneger.utility.TimeCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkScheduleServiceImpl implements WorkScheduleService {

    private ShiftTypeService shiftTypeService;

    private WorkScheduleSettingsService workScheduleSettingsService;

    private WorkScheduleSettingsRepository workScheduleSettingsRepository;

    private WorkDayService workDayService;

    private EmployeeService employeeService;

    private EmployeeShiftService employeeShiftService;

    private ShiftTypeGroupService shiftTypeGroupService;

    public WorkScheduleServiceImpl() {
    }

    @Override
    public List<WorkScheduleViewAllModel> bossOrComputerGetWorkScheduleViewAllModels(List<LocalDate> dates, Long businessUnitId) {
        WorkScheduleSettings settings = this.workScheduleSettingsRepository.getSettingsByBusinessUnitId(businessUnitId);
        int numbOfEmployeesInShift = settings.getNumbOfEmployeesInShift();
        boolean isOnDoubleShiftRegime = settings.getIsOnDoubleShiftRegime();
        boolean isOnTripleShiftRegime = settings.getIsOnTripleShiftRegime();

        //set is full by maxNumbOfEmployeesInShift
        int maxNumbOfEmployeesInShift = this.employeeService.getNumbOfActiveEmployeesInBusinessUnitAndSubUnit(businessUnitId);

        List<WorkScheduleViewAllModel> viewModels = new ArrayList<>();
        List<ShiftTypeGroup> orderedGroups = settings.getShiftTypeGroupsToObserve().stream().sorted(Comparator.comparing(ShiftTypeGroup::getGroupOrderWeight)).collect(Collectors.toList());

        for (ShiftTypeGroup shiftTypeGroup : orderedGroups) {
            ShiftTypeGroupScheduleViewModel shiftTypeGroupViewModel = this.shiftTypeGroupService.getShiftTypeGroupScheduleViewModel(shiftTypeGroup);

            WorkScheduleViewAllModel viewModel = new WorkScheduleViewAllModel();
            viewModel.setShiftTypeGroup(shiftTypeGroupViewModel);
            List<WorkShiftByGroupAndDateViewModel> shiftsByGroupAndDate = new ArrayList();
            for (LocalDate date : dates) {
                boolean mustBeOnWorkDay = false;
                boolean mustBeOnRestDay = false;
                boolean mustBeOnLastWorkDayBeforeRestDay = false;
                boolean mustBeOnLastRestDayBeforeWorkDay = false;

                WorkDay workDay = this.workDayService.getWorkDayByDate(date);
                boolean isHoliday = !(workDay.getWorkingDayType().equals(WorkingDayType.WORKDAY));
                LocalDate nextDay = date.plusDays(1L);
                WorkDay nextWorkDay = this.workDayService.getWorkDayByDate(nextDay);
                boolean isNextDayHoliday = !(nextWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY));
                if (isHoliday) {
                    if (isNextDayHoliday) {
                        mustBeOnRestDay = true;
                    } else {
                        mustBeOnLastRestDayBeforeWorkDay = true;
                    }
                } else {
                    if (isNextDayHoliday) {
                        mustBeOnLastWorkDayBeforeRestDay = true;
                    } else {
                        mustBeOnWorkDay = true;
                    }
                }

                ShiftType shiftType = null;
                List<ShiftType> shiftTypesInGroup = shiftTypeGroup.getShiftTypes();
                for (ShiftType currentShiftType : shiftTypesInGroup) {
                    if ((isOnTripleShiftRegime && currentShiftType.getCanBeOnTripleShiftRegime()) ||
                            (!isOnTripleShiftRegime && isOnDoubleShiftRegime && currentShiftType.getCanBeOnDoubleShiftRegime())) {
                        if ((mustBeOnWorkDay && currentShiftType.getCanBeOnWorkDay()) ||
                                (mustBeOnRestDay && currentShiftType.getCanBeOnRestDay()) ||
                                (mustBeOnLastWorkDayBeforeRestDay && currentShiftType.getCanBeOnLastWorkDayBeforeRestDay()) ||
                                (mustBeOnLastRestDayBeforeWorkDay && currentShiftType.getCanBeOnLastRestDayBeforeWorkDay())) {
                            shiftType = currentShiftType;
                            break;
                        }
                    }
                }

                boolean hasNoPosibleShiftOnThisDate = false;
                if (shiftType == null) {
                    hasNoPosibleShiftOnThisDate = true;
                }

                WorkShiftByGroupAndDateViewModel workShiftByGroupAndDate = new WorkShiftByGroupAndDateViewModel();
                workShiftByGroupAndDate.setHasNoPossibleShiftOnThisDate(hasNoPosibleShiftOnThisDate);
                workShiftByGroupAndDate.setDate(TimeCalculator.getDateAsString(date, "-"));
                workShiftByGroupAndDate.setShortDate(TimeCalculator.getShortDateAsString(date, "-"));
                workShiftByGroupAndDate.setIsHoliday(isHoliday);
                if (date.equals(LocalDate.now())) {
                    workShiftByGroupAndDate.setIsToday(true);
                } else {
                    workShiftByGroupAndDate.setIsToday(false);
                }

                List<EmployeeFullShortNameShiftViewModel> employees = new ArrayList<>();
                int numbOfHourToPay = 0;

                if (hasNoPosibleShiftOnThisDate) {
                    workShiftByGroupAndDate.setNumbOfEmployeesInShift(0);
                    workShiftByGroupAndDate.setIsPayShift(false);
                    workShiftByGroupAndDate.setIsFull(false);
                    workShiftByGroupAndDate.setShiftTypeId(0L);
                    workShiftByGroupAndDate.setShiftTypeShortName("");
                } else {
                    workShiftByGroupAndDate.setShiftTypeId(shiftType.getId());
                    workShiftByGroupAndDate.setShiftTypeShortName(shiftType.getShortName());
                    workShiftByGroupAndDate.setNumbOfEmployeesInShift(numbOfEmployeesInShift);
                    employees = this.employeeShiftService.getEmployeeInfoByDateAndShiftTypeByBusinessUnitAndSubUnit(date, shiftType.getId(), businessUnitId);
                    if (isHoliday) {
                        workShiftByGroupAndDate.setIsPayShift(true);
                        if (shiftType.getIsEndOnNextDay()) {
                            if (!(nextWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY))) {
                                numbOfHourToPay = 8;
                            } else {
                                LocalTime startTime = LocalTime.of(shiftType.getStartTime().getHour(), shiftType.getStartTime().getMinute());
                                LocalTime endTime = LocalTime.of(0, 0);
                                LocalTime duration = TimeCalculator.getTimeMinusTime(startTime, endTime);
                                numbOfHourToPay = duration.getHour();
                                if ((duration.getMinute() / 60) > 0.5) {
                                    numbOfHourToPay++;
                                }
                            }
                        } else {
                            numbOfHourToPay = 8;
                        }
                    } else {
                        if (shiftType.getIsEndOnNextDay()) {
                            if (!(nextWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY))) {
                                workShiftByGroupAndDate.setIsPayShift(true);

                                LocalTime startTime = LocalTime.of(0, 0);
                                LocalTime endTime = null;
                                if (shiftType.getSecondStartTime() == null) {
                                    endTime = LocalTime.of(shiftType.getEndTime().getHour(), shiftType.getEndTime().getMinute());
                                } else {
                                    endTime = LocalTime.of(shiftType.getSecondEndTime().getHour(), shiftType.getSecondEndTime().getMinute());
                                }

                                LocalTime duration = TimeCalculator.getTimeMinusTime(startTime, endTime);
                                numbOfHourToPay = duration.getHour();
                                if ((duration.getMinute() / 60) > 0.5) {
                                    numbOfHourToPay++;
                                }
                            } else {
                                workShiftByGroupAndDate.setIsPayShift(false);
                            }
                        } else {
                            workShiftByGroupAndDate.setIsPayShift(false);
                        }
                    }

                    //set is full by maxNumbOfEmployeesInShift
                    if (employees.size() == maxNumbOfEmployeesInShift) {
                        workShiftByGroupAndDate.setIsFull(true);
                    } else {
                        workShiftByGroupAndDate.setIsFull(false);
                    }
                }

                workShiftByGroupAndDate.setEmployees(employees);
                workShiftByGroupAndDate.setNumbOfHourToPay(numbOfHourToPay);


                shiftsByGroupAndDate.add(workShiftByGroupAndDate);
            }

            viewModel.setShiftsByGroupAndDate(shiftsByGroupAndDate);

            viewModels.add(viewModel);
        }

        return viewModels;
    }

    @Autowired
    public void setWorkScheduleSettingsRepository(WorkScheduleSettingsRepository workScheduleSettingsRepository) {
        this.workScheduleSettingsRepository = workScheduleSettingsRepository;
    }

    @Autowired
    public void setShiftTypeService(ShiftTypeService shiftTypeService) {
        this.shiftTypeService = shiftTypeService;
    }

    @Autowired
    public void setWorkScheduleSettingsService(WorkScheduleSettingsService workScheduleSettingsService) {
        this.workScheduleSettingsService = workScheduleSettingsService;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
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
    public void setShiftTypeGroupService(ShiftTypeGroupService shiftTypeGroupService) {
        this.shiftTypeGroupService = shiftTypeGroupService;
    }

    /*
    private List<WorkScheduleViewAllModel> getWorkScheduleViewAllModelsByDatesAndShiftTypes(List<LocalDate> dates, List<ShiftTypeGroupScheduleViewModel> shiftTypeGroups, int maxNumbOfEmployeesInShift) {
        List<WorkScheduleViewAllModel> viewModels = new ArrayList<>();
        for (ShiftTypeGroupScheduleViewModel shiftTypeGroup : shiftTypeGroups) {
            WorkScheduleViewAllModel viewModel = new WorkScheduleViewAllModel();
            viewModel.setShiftTypeGroup(shiftTypeGroup);
            List<WorkShiftByGroupAndDateViewModel> shiftsByGroupAndDate = new ArrayList();
            for (LocalDate date : dates) {
                WorkDay workDay = this.workDayService.getWorkDayByDate(date);
                WorkShiftByGroupAndDateViewModel workShiftByGroupAndDate = new WorkShiftByGroupAndDateViewModel();
                workShiftByTypeAndDate.setDate(TimeCalculator.getDateAsString(date, "-"));
                workShiftByTypeAndDate.setShortDate(TimeCalculator.getShortDateAsString(date, "-"));
                boolean isHoliday = !(workDay.getWorkingDayType().equals(WorkingDayType.WORKDAY));
                workShiftByTypeAndDate.setIsHoliday(isHoliday);
                if (date.equals(LocalDate.now())) {
                    workShiftByTypeAndDate.setIsToday(true);
                } else {
                    workShiftByTypeAndDate.setIsToday(false);
                }

                workShiftByTypeAndDate.setMaxNumbOfEmployeesInShift(maxNumbOfEmployeesInShift);
                List<EmployeeFullShortNameShiftViewModel> employees = this.employeeShiftService.getEmployeeInfoByDateAndShiftType(date, shiftType.getId());
                workShiftByTypeAndDate.setEmployees(employees);

                int numbOfHourToPay = 0;
                if (isHoliday) {
                    workShiftByTypeAndDate.setIsPayShift(true);
                    if (shiftType.getIsEndOnNextDay()) {
                        //LocalDate nextDay = date.plusDays(1L);
                        //WorkDay nextWorkDay = this.workDayService.getWorkDayByDate(nextDay);
                        //if (!(nextWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY))) {
                        //    workShiftByTypeAndDate.setIsPayShift(true);
                        //} else {
                        //    workShiftByTypeAndDate.setIsPayShift(false);
                        //}
                        LocalDate nextDay = date.plusDays(1L);
                        WorkDay nextWorkDay = this.workDayService.getWorkDayByDate(nextDay);
                        if (!(nextWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY))) {
                            numbOfHourToPay = 8;
                        } else {
                            LocalTime startTime = LocalTime.of(shiftType.getStartTime().getHour(), shiftType.getStartTime().getMinute());
                            LocalTime endTime = LocalTime.of(0, 0);
                            LocalTime duration = TimeCalculator.getTimeMinusTime(startTime, endTime);
                            numbOfHourToPay = duration.getHour();
                            if ((duration.getMinute() / 60) > 0.5) {
                                numbOfHourToPay++;
                            }
                        }
                    } else {
                        //workShiftByTypeAndDate.setIsPayShift(true);
                        numbOfHourToPay = 8;
                    }
                } else {
                    if (shiftType.getIsEndOnNextDay()) {
                        LocalDate nextDay = date.plusDays(1L);
                        WorkDay nextWorkDay = this.workDayService.getWorkDayByDate(nextDay);
                        if (!(nextWorkDay.getWorkingDayType().equals(WorkingDayType.WORKDAY))) {
                            workShiftByTypeAndDate.setIsPayShift(true);

                            LocalTime startTime = LocalTime.of(0, 0);
                            LocalTime endTime = LocalTime.of(shiftType.getEndTime().getHour(), shiftType.getEndTime().getMinute());
                            LocalTime duration = TimeCalculator.getTimeMinusTime(startTime, endTime);
                            numbOfHourToPay = duration.getHour();
                            if ((duration.getMinute() / 60) > 0.5) {
                                numbOfHourToPay++;
                            }
                        } else {
                            workShiftByTypeAndDate.setIsPayShift(false);
                        }
                    } else {
                        workShiftByTypeAndDate.setIsPayShift(false);
                    }
                }

                workShiftByTypeAndDate.setNumbOfHourToPay(numbOfHourToPay);

                //set is full by maxNumbOfEmployeesInShift
                if (maxNumbOfEmployeesInShift == employees.size()) {
                    workShiftByTypeAndDate.setIsFull(true);
                } else {
                    workShiftByTypeAndDate.setIsFull(false);
                }

                shiftsByTypeAndDate.add(workShiftByTypeAndDate);
            }

            viewModel.setShiftsByTypeAndDate(shiftsByTypeAndDate);


            viewModels.add(viewModel);
        }

        return viewModels;
    }
    */
}
