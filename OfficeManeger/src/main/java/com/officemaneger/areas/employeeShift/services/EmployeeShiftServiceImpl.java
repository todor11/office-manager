package com.officemaneger.areas.employeeShift.services;

import com.officemaneger.areas.annualLeave.entities.AnnualLeave;
import com.officemaneger.areas.annualLeave.repositories.AnnualLeaveRepository;
import com.officemaneger.areas.annualLeave.services.AnnualLeaveService;
import com.officemaneger.areas.annualLeaveDateInterval.entities.AnnualLeaveDateInterval;
import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalViewModel;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.annualLeaveType.repositories.AnnualLeaveTypeRepository;
import com.officemaneger.areas.annualLeaveType.services.AnnualLeaveTypeService;
import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.dualNameTypes.entities.DualNameType;
import com.officemaneger.areas.dualNameTypes.models.DualNameTypeViewModel;
import com.officemaneger.areas.dualNameTypes.services.DualNameTypeService;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.repositories.EmployeeRepository;
import com.officemaneger.areas.employeeShift.entities.EmployeeShift;
import com.officemaneger.areas.employeeShift.models.bindingModels.EmployeeShiftAddMultiple;
import com.officemaneger.areas.employeeShift.models.bindingModels.EmployeeShiftInWorkShiftUpdateModel;
import com.officemaneger.areas.employeeShift.models.interfaces.EmployeeFullShortNameShiftView;
import com.officemaneger.areas.employeeShift.models.interfaces.EmployeeShiftViewAll;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeFullShortNameShiftViewModel;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeShiftViewAllImpl;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeShiftViewAllModel;
import com.officemaneger.areas.employeeShift.repositories.EmployeeShiftRepository;
import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftType.repositories.ShiftTypeRepository;
import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.configs.errors.CustomFieldError;
import com.officemaneger.enums.WorkingDayType;
import com.officemaneger.utility.Constants;
import com.officemaneger.utility.LocalDateParser;
import com.officemaneger.utility.TimeCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
@Transactional
public class EmployeeShiftServiceImpl implements EmployeeShiftService {

    private ModelMapper modelMapper;

    private EmployeeShiftRepository employeeShiftRepository;

    private EmployeeRepository employeeRepository;

    private ShiftTypeRepository shiftTypeRepository;

    private WorkDayService workDayService;

    private AnnualLeaveService annualLeaveService;

    private AnnualLeaveRepository annualLeaveRepository;

    private AnnualLeaveTypeService annualLeaveTypeService;

    private AnnualLeaveTypeRepository annualLeaveTypeRepository;

    private DualNameTypeService dualNameTypeService;

    private BusinessUnitService businessUnitService;

    public EmployeeShiftServiceImpl() {
    }

    @Override
    public EmployeeShiftViewAllModel getEmployeeShiftViewAllModel(Long employeeId, LocalDate date) {
        Long employeeShiftId = this.employeeShiftRepository.getEmployeeShiftIdByEmployeeIdAndDate(employeeId, date);
        boolean hasEmployeeShift = employeeShiftId != null;
        boolean hasDualNameType = this.employeeShiftRepository.getEmployeeShiftTypeIdByEmployeeIdAndDate(employeeId, date) != null;

        EmployeeShiftViewAll employeeShiftViewAll = null;
        if (hasEmployeeShift && hasDualNameType) {
            employeeShiftViewAll = this.employeeShiftRepository.getEmployeeShiftViewAllByEmployeeIdAndDate(employeeId, date);
        } else if(hasEmployeeShift){
            employeeShiftViewAll = new EmployeeShiftViewAllImpl(employeeShiftId);
        }

        WorkDay workDay = this.workDayService.getWorkDayByDate(date);
        EmployeeShiftViewAllModel employeeShiftViewAllModel = new EmployeeShiftViewAllModel();
        if (!hasEmployeeShift) {
            EmployeeShift employeeShift = new EmployeeShift();
            employeeShift.setDate(date);
            Employee owner = this.employeeRepository.findOne(employeeId);
            if (owner == null) {
                return null;
            }
            employeeShift.setOwner(owner);
            employeeShift.setWorkDay(workDay);
            employeeShift.setHasCustomTime(false);
            EmployeeShift savedEmployeeShift = this.employeeShiftRepository.save(employeeShift);

            employeeShiftViewAllModel.setId(savedEmployeeShift.getId());
            employeeShiftViewAllModel.setDate(TimeCalculator.getDateAsString(date, "-"));
            employeeShiftViewAllModel.setShortDate(TimeCalculator.getShortDateAsString(date, "-"));
            employeeShiftViewAllModel.setTypeAsString("");
            employeeShiftViewAllModel.setTooltipText("няма въведено");
            employeeShiftViewAllModel.setHasCustomTime(false);

            //set default times to ""
            employeeShiftViewAllModel.setStartTime("");
            employeeShiftViewAllModel.setEndTime("");
            employeeShiftViewAllModel.setSecondStartTime("");
            employeeShiftViewAllModel.setSecondEndTime("");

        } else {
            //set default times to ""
            employeeShiftViewAllModel.setStartTime("");
            employeeShiftViewAllModel.setEndTime("");
            employeeShiftViewAllModel.setSecondStartTime("");
            employeeShiftViewAllModel.setSecondEndTime("");

            employeeShiftViewAllModel.setId(employeeShiftId);
            employeeShiftViewAllModel.setDate(TimeCalculator.getDateAsString(date, "-"));
            employeeShiftViewAllModel.setShortDate(TimeCalculator.getShortDateAsString(date, "-"));
            DualNameType dualNameType = employeeShiftViewAll.getDualNameType();
            if (dualNameType == null) {
                employeeShiftViewAllModel.setTypeAsString("");
                employeeShiftViewAllModel.setTooltipText("няма въведено");
            } else {
                employeeShiftViewAllModel.setTypeAsString(dualNameType.getShortName());
                DualNameTypeViewModel dualNameTypeViewModel = new DualNameTypeViewModel();
                dualNameTypeViewModel.setId(dualNameType.getId());
                dualNameTypeViewModel.setShortName(dualNameType.getShortName());
                dualNameTypeViewModel.setFullName(dualNameType.getFullName());
                //set by class
                if (dualNameType instanceof ShiftType) {
                    ShiftType shiftType = this.shiftTypeRepository.findOne(dualNameType.getId());
                    dualNameTypeViewModel.setIsRegular(shiftType.getIsRegularShift());
                    if (shiftType.getIsRegularShift()) {
                        EmployeeShift employeeShift = this.employeeShiftRepository.getEmployeeByEmployeeIdAndDate(employeeId, date);
                        employeeShiftViewAllModel.setShiftDuration(TimeCalculator.getTimeAsString(shiftType.getShiftDuration()));
                        employeeShiftViewAllModel.setBreakDuration(TimeCalculator.getTimeAsString(shiftType.getBreakDuration()));
                        employeeShiftViewAllModel.setStartTime(TimeCalculator.getTimeAsStringFromDateTime(employeeShift.getStartTime()));
                        employeeShiftViewAllModel.setEndTime(TimeCalculator.getTimeAsStringFromDateTime(employeeShift.getEndTime()));
                        employeeShiftViewAllModel.setSecondStartTime(TimeCalculator.getTimeAsStringFromDateTime(employeeShift.getSecondStartTime()));
                        employeeShiftViewAllModel.setSecondEndTime(TimeCalculator.getTimeAsStringFromDateTime(employeeShift.getSecondEndTime()));
                        employeeShiftViewAllModel.setIsEndOnNextDay(employeeShift.getIsEndOnNextDay());
                        employeeShiftViewAllModel.setHasCustomTime(employeeShift.getHasCustomTime());

                        StringBuilder sb = new StringBuilder();
                        sb.append("смяна:").append(Constants.HTML_ELEMENT_NEW_LINE);
                        sb.append("от: ").append(employeeShiftViewAllModel.getStartTime()).append(Constants.HTML_ELEMENT_NEW_LINE);
                        sb.append("до: ").append(employeeShiftViewAllModel.getEndTime());
                        if (employeeShiftViewAllModel.getSecondStartTime() != null) {
                            sb.append(Constants.HTML_ELEMENT_NEW_LINE);
                            sb.append("от: ").append(employeeShiftViewAllModel.getSecondStartTime()).append(Constants.HTML_ELEMENT_NEW_LINE);
                            sb.append("до: ").append(employeeShiftViewAllModel.getSecondEndTime());
                        }
                        employeeShiftViewAllModel.setTooltipText(sb.toString());
                    } else {
                        dualNameTypeViewModel.setIsRegular(false);
                        employeeShiftViewAllModel.setTooltipText(dualNameType.getFullName());
                    }
                } else {
                    dualNameTypeViewModel.setIsRegular(false);
                    employeeShiftViewAllModel.setTooltipText(dualNameType.getFullName());
                }

                employeeShiftViewAllModel.setType(dualNameTypeViewModel);
            }
        }

        if (date.equals(LocalDate.now())) {
            employeeShiftViewAllModel.setIsToday(true);
        } else {
            employeeShiftViewAllModel.setIsToday(false);
        }

        boolean isHoliday = !(workDay.getWorkingDayType().equals(WorkingDayType.WORKDAY));
        employeeShiftViewAllModel.setIsHoliday(isHoliday);

        employeeShiftViewAllModel.setIsTypicalWorkDay(false);
        employeeShiftViewAllModel.setIsTypicalRestDay(false);
        employeeShiftViewAllModel.setIsLastWorkDayBeforeRestDay(false);
        employeeShiftViewAllModel.setIsLastRestDayBeforeWorkDay(false);
        LocalDate nextDate = date.plusDays(1L);
        WorkDay nextWorkDay = this.workDayService.getWorkDayByDate(nextDate);
        boolean isNextDayHoliday = !TimeCalculator.getIsWorkingDayFromWorkDay(nextWorkDay);
        if (isHoliday) {
            if (isNextDayHoliday) {
                employeeShiftViewAllModel.setIsTypicalRestDay(true);
            } else {
                employeeShiftViewAllModel.setIsLastRestDayBeforeWorkDay(true);
            }
        } else {
            if (isNextDayHoliday) {
                employeeShiftViewAllModel.setIsLastWorkDayBeforeRestDay(true);
            } else {
                employeeShiftViewAllModel.setIsTypicalWorkDay(true);
            }
        }

        return employeeShiftViewAllModel;
    }

    @Override
    public boolean updateEmployeeShiftFromWorkShift(EmployeeShiftInWorkShiftUpdateModel employeeShift) {
        Long employeeId = this.employeeShiftRepository.getEmployeeIdByEmployeeShiftId(employeeShift.getId());
        if (employeeId == null || !employeeId.equals(employeeShift.getEmployeeId())) {
            return false;
        }
        Long dualNameTypeId = employeeShift.getShiftTypeId();
        boolean isNewAnnualLeaveType = this.annualLeaveTypeService.isAnnualLeaveType(dualNameTypeId);
        if (isNewAnnualLeaveType) {
            boolean hasEmployeeThisAnnualLeave = this.annualLeaveService.hasEmployeeAnnualLeave(employeeId, dualNameTypeId);
            if (!hasEmployeeThisAnnualLeave) {
                return false;
            }
        }

        EmployeeShift employeeShiftDB = this.employeeShiftRepository.findOne(employeeShift.getId());
        DualNameType oldEmployeeShiftType = employeeShiftDB.getType();
        if (oldEmployeeShiftType != null && oldEmployeeShiftType instanceof AnnualLeaveType) {
            this.annualLeaveService.increaseAnnualLeaveToEmployee(employeeId, oldEmployeeShiftType.getId());
        }

        if (employeeShift.getShiftTypeId() == 0L) {
            employeeShiftDB.setType(null);
            employeeShiftDB.setBreakDuration(null);
            employeeShiftDB.setShiftDuration(null);
            employeeShiftDB.setStartTime(null);
            employeeShiftDB.setEndTime(null);
            employeeShiftDB.setSecondStartTime(null);
            employeeShiftDB.setSecondEndTime(null);
            employeeShiftDB.setIsEndOnNextDay(false);
            employeeShiftDB.setHasCustomTime(false);
        } else {
            if (isNewAnnualLeaveType) {
                AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOne(dualNameTypeId);
                employeeShiftDB.setType(annualLeaveType);
                this.annualLeaveService.decreaseAnnualLeaveToEmployee(employeeId, dualNameTypeId);
                employeeShiftDB.setBreakDuration(null);
                employeeShiftDB.setShiftDuration(null);
                employeeShiftDB.setStartTime(null);
                employeeShiftDB.setEndTime(null);
                employeeShiftDB.setSecondStartTime(null);
                employeeShiftDB.setSecondEndTime(null);
                employeeShiftDB.setIsEndOnNextDay(false);
                employeeShiftDB.setHasCustomTime(false);
            } else {
                ShiftType shiftType = this.shiftTypeRepository.findOne(dualNameTypeId);
                employeeShiftDB.setType(shiftType);
                employeeShiftDB.setBreakDuration(shiftType.getBreakDuration());
                employeeShiftDB.setShiftDuration(shiftType.getShiftDuration());
                employeeShiftDB.setStartTime(employeeShift.getStartTime());
                employeeShiftDB.setEndTime(employeeShift.getEndTime());
                employeeShiftDB.setSecondStartTime(employeeShift.getSecondStartTime());
                employeeShiftDB.setSecondEndTime(employeeShift.getSecondEndTime());
                employeeShiftDB.setIsEndOnNextDay(employeeShift.getIsEndOnNextDay());

                //check has Custom Time
                boolean hasCustomTime = true;
                if (shiftType.getIsRegularShift() && (employeeShiftDB.getStartTime() != null && employeeShiftDB.getEndTime() != null)) {
                    if (employeeShiftDB.getStartTime().toLocalTime().equals(shiftType.getStartTime()) &&
                            employeeShiftDB.getEndTime().toLocalTime().equals(shiftType.getEndTime())) {
                        if (employeeShiftDB.getSecondStartTime() == null && employeeShiftDB.getSecondEndTime() == null &&
                                employeeShift.getSecondStartTime() == null && employeeShift.getSecondEndTime() == null) {
                            hasCustomTime = false;
                        } else if (employeeShiftDB.getSecondStartTime() != null && employeeShiftDB.getSecondEndTime() != null &&
                                    employeeShift.getSecondStartTime() != null && employeeShift.getSecondEndTime() != null) {
                            if (employeeShiftDB.getSecondStartTime().toLocalTime().equals(shiftType.getSecondStartTime()) &&
                                    employeeShiftDB.getSecondEndTime().toLocalTime().equals(shiftType.getSecondEndTime())) {
                                hasCustomTime = false;
                            }
                        }
                    }
                }
                if (!shiftType.getIsRegularShift()) {
                    hasCustomTime = false;
                }
                employeeShiftDB.setHasCustomTime(hasCustomTime);

                if (employeeShiftDB.getStartTime() != null && employeeShiftDB.getStartTime().toLocalTime().equals(shiftType.getStartTime()) &&
                        employeeShiftDB.getEndTime() != null && employeeShiftDB.getEndTime().toLocalTime().equals(shiftType.getEndTime()) &&
                        employeeShiftDB.getSecondStartTime() == null && shiftType.getSecondStartTime() == null &&
                        employeeShiftDB.getSecondEndTime() == null && shiftType.getSecondEndTime() == null) {
                    employeeShiftDB.setIsEndOnNextDay(shiftType.getIsEndOnNextDay());
                } else if (employeeShiftDB.getStartTime() != null && employeeShiftDB.getStartTime().toLocalTime().equals(shiftType.getStartTime()) &&
                        employeeShiftDB.getEndTime() != null && employeeShiftDB.getEndTime().toLocalTime().equals(shiftType.getEndTime()) &&
                        employeeShiftDB.getSecondStartTime() != null && shiftType.getSecondStartTime() != null &&
                        employeeShiftDB.getSecondEndTime() != null && shiftType.getSecondEndTime() != null &&
                        employeeShiftDB.getSecondStartTime().toLocalTime().equals(shiftType.getSecondStartTime()) &&
                        employeeShiftDB.getSecondEndTime().toLocalTime().equals(shiftType.getSecondEndTime())) {
                    employeeShiftDB.setIsEndOnNextDay(shiftType.getIsEndOnNextDay());
                } else {
                    boolean isEndOnNextDay = false;
                    LocalDate shiftStartDate = null;
                    LocalDate shiftEndDate = null;
                    if (employeeShift.getSecondEndTime() != null && employeeShift.getEndTime() != null) {
                        shiftStartDate = employeeShift.getStartTime().toLocalDate();
                        shiftEndDate = employeeShift.getSecondEndTime().toLocalDate();
                    } else if (employeeShift.getEndTime() != null) {
                        shiftStartDate = employeeShift.getStartTime().toLocalDate();
                        shiftEndDate = employeeShift.getEndTime().toLocalDate();
                    }

                    if (shiftStartDate != null && shiftEndDate != null) {
                        isEndOnNextDay = shiftEndDate.isAfter(shiftStartDate);
                    }
                    employeeShiftDB.setIsEndOnNextDay(isEndOnNextDay);
                }
            }
        }

        //calculate shift duration if has custom times
        if (employeeShiftDB.getHasCustomTime()) {
            employeeShiftDB.setBreakDuration(null);
            LocalTime shiftDuration = TimeCalculator.getDateTimeMinusDateTimeInTime(employeeShiftDB.getStartTime(), employeeShiftDB.getEndTime());
            if (employeeShiftDB.getSecondStartTime() != null) {
                LocalTime secondTime = TimeCalculator.getDateTimeMinusDateTimeInTime(employeeShiftDB.getSecondStartTime(), employeeShiftDB.getSecondEndTime());
                shiftDuration = TimeCalculator.getTimePlusTime(shiftDuration, secondTime);
            }

            employeeShiftDB.setShiftDuration(shiftDuration);
        }

        return true;
    }

    @Override
    public boolean updateOrAddMultipleEmployeeShiftsFromWorkShift(EmployeeShiftAddMultiple employeeShiftAddMultiple) {
        Employee employee = this.employeeRepository.findOne(employeeShiftAddMultiple.getEmployeeId());
        if (employee == null) {
            return false;
        }

        Long dualNameTypeId = employeeShiftAddMultiple.getShiftTypeId();
        LocalDate startDate = employeeShiftAddMultiple.getStartDate();
        LocalDate endDate = employeeShiftAddMultiple.getEndDate();
        boolean addToHolidayToo = employeeShiftAddMultiple.getAddToHolidayToo();
        boolean isAnnualLeave = employeeShiftAddMultiple.getIsAnnualLeave();
        int numbOfAnnualLeave = employeeShiftAddMultiple.getNumbOfAnnualLeave();

        return this.updateOrAddMultiple(employee, dualNameTypeId, startDate, endDate, addToHolidayToo, isAnnualLeave, numbOfAnnualLeave);
    }

    @Override
    public AnnualLeaveDateIntervalViewModel updateOrAddEmployeeShiftsMultipleAnnualLeave(Employee employee, AnnualLeaveDateInterval dateInterval) {
        Long dualNameTypeId = dateInterval.getType().getId();
        LocalDate startDate = dateInterval.getStartDate();
        LocalDate endDate = dateInterval.getEndDate();
        boolean addToHolidayToo = false;
        boolean isAnnualLeave = true;
        Long numbOfAnnualLeaveAsLong = this.workDayService.getNumbOfWorkingDaysBetweenDates(startDate, endDate);
        int numbOfAnnualLeave;
        if ( numbOfAnnualLeaveAsLong > (long)Integer.MAX_VALUE ) {
            // too big numbOfAnnualLeave
            FieldError fieldError = new FieldError("dateIntervals", "dateIntervals", "error.canNotConvertLongToInt");
            throw new CustomFieldError("dateIntervals", fieldError);
        }
        else {
            numbOfAnnualLeave = numbOfAnnualLeaveAsLong.intValue();
        }

        boolean isAdded = this.updateOrAddMultiple(employee, dualNameTypeId, startDate, endDate, addToHolidayToo, isAnnualLeave, numbOfAnnualLeave);
        if (!isAdded) {
            return null;
        }

        AnnualLeaveDateIntervalViewModel viewModel = new AnnualLeaveDateIntervalViewModel();
        viewModel.setStartDate(LocalDateParser.getLocalDateAsString(startDate, "-"));
        viewModel.setEndDate(LocalDateParser.getLocalDateAsString(endDate, "-"));
        viewModel.setShiftTypeShortName(this.dualNameTypeService.getShortNameType(dualNameTypeId));
        viewModel.setNumbOfAnnualLeave(numbOfAnnualLeave);

        return viewModel;
    }

    @Override
    public List<EmployeeFullShortNameShiftViewModel> getEmployeeInfoByDateAndShiftTypeAndBusinessUnit(LocalDate date, Long shiftTypeId, Long businessUnitId) {
        List<EmployeeFullShortNameShiftView> employees = this.employeeShiftRepository.getEmployeeInfoByDateAndShiftTypeAndBusinessUnit(date, shiftTypeId, businessUnitId);
        return this.convertToEmployeeFullShortNameShiftViewModel(employees);
    }

    @Override
    public List<EmployeeFullShortNameShiftViewModel> getEmployeeInfoByDateAndShiftTypeByBusinessUnitAndSubUnit(LocalDate date, Long shiftTypeId, Long businessUnitId) {
        List<EmployeeFullShortNameShiftView> employees = new ArrayList<>();
        this.recursivelyGetEmployeesInfoByDateAndShiftTypeAndBusinessUnitAndSubUnit(date, shiftTypeId, businessUnitId, employees);
        return this.convertToEmployeeFullShortNameShiftViewModel(employees);
    }

    @Override
    public Map<LocalDate, LocalTime> getSingleEmployeeOverPaidHours(Long employeeId, List<LocalDate> restDays) {
        Map<LocalDate, LocalTime> result = new LinkedHashMap<>();
        List<LocalDate> shiftDatesToObserve = new ArrayList<>();
        shiftDatesToObserve.addAll(restDays);
        for (LocalDate restDay : restDays) {
            LocalDate prevDay = restDay.minusDays(1l);
            if (!shiftDatesToObserve.contains(prevDay)) {
                shiftDatesToObserve.add(prevDay);
            }
        }

        List<EmployeeShift> employeeShifts = new ArrayList<>();
        if (shiftDatesToObserve.size() > 0) {
            employeeShifts = this.employeeShiftRepository.getEmployeeShiftsByEmployeeAndDatesOrd(employeeId, shiftDatesToObserve);
        }

        for (LocalDate restDay : restDays) {
            result.put(restDay, LocalTime.of(0,0));
        }

        for (EmployeeShift employeeShift : employeeShifts) {
            if (employeeShift.getStartTime() == null) {
                continue;
            }

            LocalDate currentDate = employeeShift.getDate();
            LocalTime newTime = result.get(currentDate);
            if (newTime == null) {
                newTime = LocalTime.of(0,0);
            }

            if (result.containsKey(currentDate)) {
                if (employeeShift.getIsEndOnNextDay()) {
                    LocalTime nextDayDuration = LocalTime.of(0,0);
                    if (employeeShift.getSecondStartTime() != null) {
                        nextDayDuration = LocalTime.of(employeeShift.getSecondEndTime().getHour(), employeeShift.getSecondEndTime().getMinute());
                    } else {
                        nextDayDuration = LocalTime.of(employeeShift.getEndTime().getHour(), employeeShift.getEndTime().getMinute());
                    }

                    nextDayDuration = TimeCalculator.getTimeMinusTime(employeeShift.getBreakDuration(), nextDayDuration);
                    if (result.containsKey(currentDate.plusDays(1L))) {
                        LocalTime nextDayTime = result.get(currentDate.plusDays(1L));
                        nextDayTime = TimeCalculator.getTimePlusTime(nextDayTime, nextDayDuration);
                        result.put(currentDate.plusDays(1L), nextDayTime);
                    }

                    LocalTime currentPayTime = TimeCalculator.getTimeMinusTime(nextDayDuration, employeeShift.getShiftDuration());
                    newTime = TimeCalculator.getTimePlusTime(newTime, currentPayTime);
                } else {
                    newTime = TimeCalculator.getTimePlusTime(newTime, employeeShift.getShiftDuration());
                }

                result.put(currentDate, newTime);
            } else {
                if (employeeShift.getIsEndOnNextDay()) {
                    LocalTime nextDayDuration = LocalTime.of(0,0);
                    if (employeeShift.getSecondStartTime() != null) {
                        nextDayDuration = LocalTime.of(employeeShift.getSecondEndTime().getHour(), employeeShift.getSecondEndTime().getMinute());
                    } else {
                        nextDayDuration = LocalTime.of(employeeShift.getEndTime().getHour(), employeeShift.getEndTime().getMinute());
                    }

                    newTime = result.get(currentDate.plusDays(1L));
                    newTime = TimeCalculator.getTimePlusTime(newTime, nextDayDuration);
                    result.put(currentDate.plusDays(1L), newTime);
                }
            }
        }

        List<LocalDate> dataToDelete = new ArrayList<>();
        for (Map.Entry<LocalDate, LocalTime> timeEntry : result.entrySet()) {
            if (timeEntry.getValue().getHour() == 0 && timeEntry.getValue().getMinute() == 0) {
                dataToDelete.add(timeEntry.getKey());
            }
        }
        for (LocalDate localDate : dataToDelete) {
            result.remove(localDate);
        }

        return result;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @Autowired
    public void setEmployeeShiftRepository(EmployeeShiftRepository employeeShiftRepository) {
        this.employeeShiftRepository = employeeShiftRepository;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setShiftTypeRepository(ShiftTypeRepository shiftTypeRepository) {
        this.shiftTypeRepository = shiftTypeRepository;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    @Autowired
    public void setAnnualLeaveService(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }

    @Autowired
    public void setAnnualLeaveTypeService(AnnualLeaveTypeService annualLeaveTypeService) {
        this.annualLeaveTypeService = annualLeaveTypeService;
    }

    @Autowired
    public void setAnnualLeaveTypeRepository(AnnualLeaveTypeRepository annualLeaveTypeRepository) {
        this.annualLeaveTypeRepository = annualLeaveTypeRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setAnnualLeaveRepository(AnnualLeaveRepository annualLeaveRepository) {
        this.annualLeaveRepository = annualLeaveRepository;
    }

    @Autowired
    public void setDualNameTypeService(DualNameTypeService dualNameTypeService) {
        this.dualNameTypeService = dualNameTypeService;
    }

    private void recursivelyGetEmployeesInfoByDateAndShiftTypeAndBusinessUnitAndSubUnit(LocalDate date, Long shiftTypeId, Long businessUnitId, List<EmployeeFullShortNameShiftView> employees) {
        employees.addAll(this.employeeShiftRepository.getEmployeeInfoByDateAndShiftTypeAndBusinessUnit(date, shiftTypeId, businessUnitId));
        List<Long> subUnitsIds = this.businessUnitService.getSubUnitsIds(businessUnitId);
        for (Long subUnitId : subUnitsIds) {
            this.recursivelyGetEmployeesInfoByDateAndShiftTypeAndBusinessUnitAndSubUnit(date, shiftTypeId, subUnitId, employees);
        }
    }

    private List<EmployeeFullShortNameShiftViewModel> convertToEmployeeFullShortNameShiftViewModel(List<EmployeeFullShortNameShiftView> employees) {
        List<EmployeeFullShortNameShiftViewModel> viewModels = new ArrayList<>();
        for (EmployeeFullShortNameShiftView employee : employees) {
            EmployeeFullShortNameShiftViewModel employeeInfoModel = new EmployeeFullShortNameShiftViewModel();
            employeeInfoModel.setEmployeeId(employee.getEmployeeId());
            employeeInfoModel.setEmployeeShiftId(employee.getEmployeeShiftId());
            String employeeNames = employee.getEmployeeFirstName();
            if (employee.getEmployeeMiddleName() != null && !employee.getEmployeeMiddleName().isEmpty()) {
                String middleName = employee.getEmployeeMiddleName();
                middleName = middleName.charAt(0) + ". ";
                employeeNames = employeeNames + " " + middleName;
            }

            employeeNames = employeeNames + " " + employee.getEmployeeLastName();
            employeeInfoModel.setEmployeeFullShortName(employeeNames);

            viewModels.add(employeeInfoModel);
        }

        return viewModels;
    }

    private boolean updateOrAddMultiple(Employee employee, Long dualNameTypeId, LocalDate startDate, LocalDate endDate, boolean addToHolidayToo, boolean isAnnualLeave, int numbOfAnnualLeave) {
        if (isAnnualLeave) {
            for (AnnualLeave annualLeave : employee.getAnnualLeaves()) {
                if (annualLeave.getType().getId().equals(dualNameTypeId)) {
                    if (annualLeave.getNumberOfAnnualLeave() >= numbOfAnnualLeave) {
                        List<LocalDate> datesToSetAnnualLeave = this.workDayService.getWorkDaysFromDateToDate(startDate, endDate);
                        if (datesToSetAnnualLeave.size() != numbOfAnnualLeave) {
                            FieldError fieldError = new FieldError("dateIntervals", "dateIntervals", "error.employeeShiftAddMultipleAnnualLeave.notValidNumbOfAnnualLeave");
                            throw new CustomFieldError("dateIntervals", fieldError);
                        }

                        for (LocalDate date : datesToSetAnnualLeave) {
                            EmployeeShift employeeShift = this.employeeShiftRepository.getEmployeeByEmployeeIdAndDate(employee.getId(), date);
                            if (employeeShift == null) {
                                //create new employeeShift
                                employeeShift = new EmployeeShift();
                                employeeShift.setDate(date);
                                employeeShift.setOwner(employee);
                                WorkDay workDay = this.workDayService.getWorkDayByDate(date);
                                employeeShift.setWorkDay(workDay);
                                employeeShift.setType(null);
                                employeeShift.setBreakDuration(null);
                                employeeShift.setShiftDuration(null);
                                employeeShift.setStartTime(null);
                                employeeShift.setEndTime(null);
                                employeeShift.setSecondStartTime(null);
                                employeeShift.setSecondEndTime(null);
                                employeeShift.setIsEndOnNextDay(false);
                                employeeShift.setHasCustomTime(false);

                                employeeShift = this.employeeShiftRepository.save(employeeShift);
                            } else {
                                //check prev shift type
                                if (employeeShift.getType() != null) {
                                    boolean isPrevAnnualLeave = this.annualLeaveTypeService.isAnnualLeaveType(employeeShift.getType().getId());
                                    if (isPrevAnnualLeave) {
                                        AnnualLeaveType annualLeaveType = (AnnualLeaveType) employeeShift.getType();
                                        AnnualLeave employeeOldAnnualLeave = this.annualLeaveRepository.findOneByTypeAndOwner(annualLeaveType, employee);
                                        employeeOldAnnualLeave.setNumberOfAnnualLeave(employeeOldAnnualLeave.getNumberOfAnnualLeave() + 1);
                                    } else {
                                        employeeShift.setBreakDuration(null);
                                        employeeShift.setShiftDuration(null);
                                        employeeShift.setStartTime(null);
                                        employeeShift.setEndTime(null);
                                        employeeShift.setSecondStartTime(null);
                                        employeeShift.setSecondEndTime(null);
                                        employeeShift.setIsEndOnNextDay(false);
                                        employeeShift.setHasCustomTime(false);
                                    }
                                }
                            }
                            //set new annualLeave to employee shift
                            employeeShift.setType(annualLeave.getType());
                            annualLeave.setNumberOfAnnualLeave(annualLeave.getNumberOfAnnualLeave() - 1);
                        }

                        return true;
                    } else {
                        FieldError fieldError = new FieldError("dateIntervals", "dateIntervals", "error.employeeShiftAddMultipleAnnualLeave.notValidNumbOfAnnualLeave");
                        throw new CustomFieldError("dateIntervals", fieldError);
                    }
                }
            }

            FieldError fieldError = new FieldError("dateIntervals", "dateIntervals", "error.employeeShiftAddMultipleAnnualLeave.notValidAnnualLeave");
            throw new CustomFieldError("dateIntervals", fieldError);
        } else {
            // update or create employee shift by shift type
            ShiftType newShiftType = this.shiftTypeRepository.findOne(dualNameTypeId);
            if (newShiftType == null) {
                FieldError fieldError = new FieldError("dateIntervals", "dateIntervals", "error.employeeShiftAddMultiple.notValidShiftType");
                throw new CustomFieldError("dateIntervals", fieldError);
            }

            //set date to add
            List<LocalDate> datesToAdd = new ArrayList<>();
            if (addToHolidayToo) {
                LocalDate date = startDate;
                while (!date.isAfter(endDate)) {
                    datesToAdd.add(date);
                    date = date.plusDays(1L);
                }
            } else {

                datesToAdd = this.workDayService.getWorkDaysFromDateToDate(startDate, endDate);
            }

            for (LocalDate date : datesToAdd) {
                EmployeeShift employeeShift = this.employeeShiftRepository.getEmployeeByEmployeeIdAndDate(employee.getId(), date);
                if (employeeShift == null) {
                    //create new employeeShift
                    employeeShift = new EmployeeShift();
                    employeeShift.setDate(date);
                    employeeShift.setOwner(employee);
                    WorkDay workDay = this.workDayService.getWorkDayByDate(date);
                    employeeShift.setWorkDay(workDay);
                    employeeShift.setType(null);
                    employeeShift.setBreakDuration(null);
                    employeeShift.setShiftDuration(null);
                    employeeShift.setStartTime(null);
                    employeeShift.setEndTime(null);
                    employeeShift.setSecondStartTime(null);
                    employeeShift.setSecondEndTime(null);
                    employeeShift.setIsEndOnNextDay(false);
                    employeeShift.setHasCustomTime(false);

                    employeeShift = this.employeeShiftRepository.save(employeeShift);
                } else {
                    //check prev shift type
                    if (employeeShift.getType() != null) {
                        boolean isPrevAnnualLeave = this.annualLeaveTypeService.isAnnualLeaveType(employeeShift.getType().getId());
                        if (isPrevAnnualLeave) {
                            AnnualLeaveType annualLeaveType = (AnnualLeaveType) employeeShift.getType();
                            AnnualLeave employeeOldAnnualLeave = this.annualLeaveRepository.findOneByTypeAndOwner(annualLeaveType, employee);
                            employeeOldAnnualLeave.setNumberOfAnnualLeave(employeeOldAnnualLeave.getNumberOfAnnualLeave() + 1);
                        }
                    }
                }

                employeeShift.setType(newShiftType);
                employeeShift.setHasCustomTime(false);
                employeeShift.setShiftDuration(newShiftType.getShiftDuration());
                employeeShift.setBreakDuration(newShiftType.getBreakDuration());
                employeeShift.setIsEndOnNextDay(newShiftType.getIsEndOnNextDay());
                if (newShiftType.getStartTime() == null) {
                    employeeShift.setStartTime(null);
                } else {
                    employeeShift.setStartTime(LocalDateTime.of(date, newShiftType.getStartTime()));
                }

                if (newShiftType.getIsEndOnNextDay()) {
                    if (newShiftType.getSecondStartTime() == null) {
                        if (newShiftType.getEndTime() == null) {
                            employeeShift.setEndTime(null);
                        } else {
                            employeeShift.setEndTime(LocalDateTime.of(date.plusDays(1L), newShiftType.getEndTime()));
                        }

                        employeeShift.setSecondStartTime(null);
                        employeeShift.setSecondEndTime(null);
                    } else {
                        if (newShiftType.getEndTime() == null) {
                            employeeShift.setEndTime(null);
                        } else {
                            employeeShift.setEndTime(LocalDateTime.of(date, newShiftType.getEndTime()));
                        }

                        if (newShiftType.getSecondStartTime() == null) {
                            employeeShift.setSecondStartTime(null);
                        } else {
                            employeeShift.setSecondStartTime(LocalDateTime.of(date, newShiftType.getSecondStartTime()));
                        }

                        if (newShiftType.getSecondEndTime() == null) {
                            employeeShift.setSecondEndTime(null);
                        } else {
                            employeeShift.setSecondEndTime(LocalDateTime.of(date.plusDays(1L), newShiftType.getSecondEndTime()));
                        }
                    }
                } else {
                    if (newShiftType.getEndTime() == null) {
                        employeeShift.setEndTime(null);
                    } else {
                        employeeShift.setEndTime(LocalDateTime.of(date, newShiftType.getEndTime()));
                    }

                    if (newShiftType.getSecondStartTime() == null) {
                        employeeShift.setSecondStartTime(null);
                        employeeShift.setSecondEndTime(null);
                    } else {
                        employeeShift.setSecondStartTime(LocalDateTime.of(date, newShiftType.getSecondStartTime()));
                        employeeShift.setSecondEndTime(LocalDateTime.of(date, newShiftType.getSecondEndTime()));
                    }
                }
            }

            return true;
        }
    }
}
