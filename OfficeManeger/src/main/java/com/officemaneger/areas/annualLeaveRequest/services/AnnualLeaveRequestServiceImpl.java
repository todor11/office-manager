package com.officemaneger.areas.annualLeaveRequest.services;

import com.officemaneger.areas.annualLeave.entities.AnnualLeave;
import com.officemaneger.areas.annualLeave.services.AnnualLeaveService;
import com.officemaneger.areas.annualLeaveDateInterval.entities.AnnualLeaveDateInterval;
import com.officemaneger.areas.annualLeaveDateInterval.models.bindingModels.AnnualLeaveDateIntervalCreationModel;
import com.officemaneger.areas.annualLeaveDateInterval.models.bindingModels.AnnualLeaveDateIntervalUpdateModel;
import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalUpdateViewModel;
import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalViewModel;
import com.officemaneger.areas.annualLeaveDateInterval.repositories.AnnualLeaveDateIntervalRepository;
import com.officemaneger.areas.annualLeaveDateInterval.services.AnnualLeaveDateIntervalService;
import com.officemaneger.areas.annualLeaveRequest.entities.AnnualLeaveRequest;
import com.officemaneger.areas.annualLeaveRequest.models.bindingModels.AnnualLeaveRequestUpdateModel;
import com.officemaneger.areas.annualLeaveRequest.models.bindingModels.AnnualLeaveRequestUserCreationModel;
import com.officemaneger.areas.annualLeaveRequest.models.viewModels.AnnualLeaveRequestUpdateViewModel;
import com.officemaneger.areas.annualLeaveRequest.models.viewModels.AnnualLeaveRequestViewModel;
import com.officemaneger.areas.annualLeaveRequest.repositories.AnnualLeaveRequestRepository;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.annualLeaveType.repositories.AnnualLeaveTypeRepository;
import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.dateInterval.entities.DateInterval;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.repositories.EmployeeRepository;
import com.officemaneger.areas.employeeShift.entities.EmployeeShift;
import com.officemaneger.areas.employeeShift.services.EmployeeShiftService;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.configs.errors.CustomFieldError;
import com.officemaneger.utility.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnnualLeaveRequestServiceImpl implements AnnualLeaveRequestService {

    private AnnualLeaveRequestRepository annualLeaveRequestRepository;

    private EmployeeRepository employeeRepository;

    private AnnualLeaveTypeRepository annualLeaveTypeRepository;

    private WorkDayService workDayService;

    private AnnualLeaveService annualLeaveService;

    private AnnualLeaveDateIntervalService annualLeaveDateIntervalService;

    private AnnualLeaveDateIntervalRepository annualLeaveDateIntervalRepository;

    private BusinessUnitService businessUnitService;

    private EmployeeShiftService employeeShiftService;

    public AnnualLeaveRequestServiceImpl() {
    }

    @Override
    public boolean userCreateRequest(AnnualLeaveRequestUserCreationModel requestCreationModel) {
        Employee employee = this.employeeRepository.findOne(requestCreationModel.getEmployeeId());
        if (employee == null) {
            return false;
        }

        AnnualLeaveRequest annualLeaveRequest = new AnnualLeaveRequest();
        annualLeaveRequest.setEmployee(employee);
        annualLeaveRequest.setCreationDateTime(LocalDateTime.now());
        annualLeaveRequest.setIsApproved(false);
        annualLeaveRequest.setIsViewed(false);

        boolean deleteAllNotValid = false;

        List<AnnualLeaveDateInterval> dateIntervals = new ArrayList<>();
        for (AnnualLeaveDateIntervalCreationModel dateIntervalModel : requestCreationModel.getAnnualLeaveDateIntervals()) {
            AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOne(dateIntervalModel.getAnnualLeaveTypeId());
            if (annualLeaveType == null) {
                deleteAllNotValid = true;
                break;
            }

            Long numbOfWorkdays = this.workDayService.getNumbOfWorkingDaysBetweenDates(dateIntervalModel.getStartDate(), dateIntervalModel.getEndDate());
            boolean annualLeaveNotValid = true;
            for (AnnualLeave annualLeave : employee.getAnnualLeaves()) {
                if (annualLeave.getType().getId().equals(annualLeaveType.getId())) {
                    Long numbOfAllAnnualLeave  = Long.valueOf(annualLeave.getNumberOfAnnualLeave());
                    if (numbOfAllAnnualLeave >= numbOfWorkdays) {
                        annualLeaveNotValid = false;
                    }

                    break;
                }
            }
            if (annualLeaveNotValid) {
                deleteAllNotValid = true;
                break;
            }

            AnnualLeaveDateInterval dateInterval = this.annualLeaveDateIntervalService.createDateInterval(annualLeaveType, dateIntervalModel.getStartDate(), dateIntervalModel.getEndDate());
            dateInterval.setAnnualLeaveRequest(annualLeaveRequest);

            dateIntervals.add(dateInterval);
        }

        if (deleteAllNotValid) {
            for (AnnualLeaveDateInterval dateInterval : dateIntervals) {
                this.annualLeaveDateIntervalService.deleteDateInterval(dateInterval);
            }

            return false;
        }

        annualLeaveRequest.setAnnualLeaveDateIntervals(dateIntervals);
        this.annualLeaveRequestRepository.save(annualLeaveRequest);
        return true;
    }

    @Override
    public List<AnnualLeaveRequestViewModel> bossGetRequests(Long bossId) {
        if (bossId == null) {
            return null;
        }

        Long businessUnitId = this.employeeRepository.getBusinessUnitIdByEmployeeId(bossId);
        if (businessUnitId == null) {
            return null;
        }

        Long officialBossId = this.businessUnitService.getPermanentBossIdByBusinessUnit(businessUnitId);
        List<AnnualLeaveRequest> requests = this.annualLeaveRequestRepository.getAllByBusinessUnit(businessUnitId, false);
        List<AnnualLeaveRequest> requestsToRemove = new ArrayList<>();
        for (AnnualLeaveRequest request : requests) {
            if (request.getEmployee().getId().equals(bossId) || request.getEmployee().getId().equals(officialBossId)) {
                requestsToRemove.add(request);
            }
        }

        for (AnnualLeaveRequest annualLeaveRequest : requestsToRemove) {
            requests.remove(annualLeaveRequest);
        }

        //add sub units boss
        List<Long> subBusinessUnitsIds = this.businessUnitService.getSubUnitsIds(businessUnitId);
        for (Long subBusinessUnitId : subBusinessUnitsIds) {
            List<Long> subUnitBossesIds = this.businessUnitService.getBossesIdsByBusinessUnit(subBusinessUnitId);
            for (Long subUnitBossesId : subUnitBossesIds) {
                requests.addAll(this.annualLeaveRequestRepository.getRequestByEmployee(subUnitBossesId, false));
            }
        }

        List<AnnualLeaveRequestViewModel> viewModels = new ArrayList<>();
        for (AnnualLeaveRequest request : requests) {
            AnnualLeaveRequestViewModel viewModel = new AnnualLeaveRequestViewModel();
            viewModel.setId(request.getId());
            viewModel.setCreationDateTime(this.convertDateTimeToString(request.getCreationDateTime()));
            String employeeFullNameAndOfficiaryId = request.getEmployee().getFullName() + " - " + request.getEmployee().getOfficiaryID();
            viewModel.setEmployeeFullNameAndOfficiaryId(employeeFullNameAndOfficiaryId);
            viewModel.setEmployeeId(request.getEmployee().getId());
            LocalDate startDate = request.getAnnualLeaveDateIntervals().get(0).getStartDate();
            LocalDate endDate = request.getAnnualLeaveDateIntervals().get(request.getAnnualLeaveDateIntervals().size() - 1).getEndDate();
            viewModel.setStartDate(startDate);
            viewModel.setEndDate(endDate);
            viewModel.setNumbOfDays(this.workDayService.getNumbOfWorkingDaysBetweenDates(startDate, endDate));
            viewModel.setIsViewed(request.getIsViewed());

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < request.getAnnualLeaveDateIntervals().size(); i++) {
                AnnualLeaveDateInterval annualLeaveDateInterval = request.getAnnualLeaveDateIntervals().get(i);
                sb.append(annualLeaveDateInterval.getType().getFullName());
                sb.append(" - ");
                sb.append(this.workDayService.getNumbOfWorkingDaysBetweenDates(annualLeaveDateInterval.getStartDate(), annualLeaveDateInterval.getEndDate()));
                sb.append(" дни;");
                if (i < request.getAnnualLeaveDateIntervals().size() - 1) {
                    sb.append(Constants.HTML_ELEMENT_NEW_LINE);
                }
            }
            viewModel.setTypeAndNumbOfDays(sb.toString());

            viewModels.add(viewModel);
            request.setIsViewed(true);
        }

        return viewModels;
    }

    @Override
    public List<AnnualLeaveDateIntervalViewModel> executeEmployeeRequest(Long requestId) {
        AnnualLeaveRequest annualLeaveRequest = this.annualLeaveRequestRepository.findOne(requestId);
        if (annualLeaveRequest == null) {
            return null;
        }

        annualLeaveRequest.setIsApproved(true);
        Employee employee = annualLeaveRequest.getEmployee();
        List<AnnualLeaveDateIntervalViewModel> viewModels = new ArrayList<>();
        List<AnnualLeaveDateInterval> dateIntervals = annualLeaveRequest.getAnnualLeaveDateIntervals();
        for (AnnualLeaveDateInterval dateInterval : dateIntervals) {
            AnnualLeaveDateIntervalViewModel currentDateIntervals = this.employeeShiftService.updateOrAddEmployeeShiftsMultipleAnnualLeave(employee, dateInterval);
            if (currentDateIntervals == null) {
                return null;
            }
            viewModels.add(currentDateIntervals);
        }

        //TODO delete request, week after execution
        annualLeaveRequest.setExecutionDateTime(LocalDateTime.now());
        //this.annualLeaveRequestRepository.delete(annualLeaveRequest);

        return viewModels;
    }

    @Override
    public AnnualLeaveRequestUpdateViewModel employeeGetUpdateRequestModel(Long employeeId, Long requestId) {
        List<AnnualLeaveDateIntervalUpdateViewModel> annualLeaveDateIntervalsViewModels = this.annualLeaveDateIntervalService.getAnnualLeaveDateIntervalUpdateViewModelsByRequest(requestId);
        AnnualLeaveRequestUpdateViewModel requestViewModel = new AnnualLeaveRequestUpdateViewModel();
        requestViewModel.setId(requestId);
        requestViewModel.setAnnualLeaveDateIntervals(annualLeaveDateIntervalsViewModels);

        return requestViewModel;
    }

    @Override
    public boolean userUpdateRequest(AnnualLeaveRequestUpdateModel annualLeaveRequestModel) {
        AnnualLeaveRequest request = this.annualLeaveRequestRepository.findOne(annualLeaveRequestModel.getId());
        if (request == null) {
            return false;
        }

        List<AnnualLeaveDateInterval> annualLeaveDateIntervalsFromDB = request.getAnnualLeaveDateIntervals();
        List<AnnualLeaveDateInterval> intervalsToDelete = new ArrayList<>();
        for (AnnualLeaveDateInterval annualLeaveDateInterval : annualLeaveDateIntervalsFromDB) {
            intervalsToDelete.add(annualLeaveDateInterval);
        }

        for (AnnualLeaveDateIntervalUpdateModel intervalUpdateModel : annualLeaveRequestModel.getAnnualLeaveDateIntervals()) {
            if (intervalUpdateModel.getId() == null || intervalUpdateModel.getId().equals(0L)) {
                //new date interval
                AnnualLeaveDateInterval newDateInterval = new AnnualLeaveDateInterval();
                newDateInterval.setStartDate(intervalUpdateModel.getStartDate());
                newDateInterval.setEndDate(intervalUpdateModel.getEndDate());
                AnnualLeaveType newType = this.annualLeaveTypeRepository.findOne(intervalUpdateModel.getAnnualLeaveTypeId());
                if (newType == null) {
                    FieldError fieldError = new FieldError("annualLeaveType", "annualLeaveType", "error.annualLeaveType.notValidId");
                    throw new CustomFieldError("annualLeaveType", fieldError);
                }
                newDateInterval.setType(newType);
                newDateInterval.setAnnualLeaveRequest(request);
                newDateInterval = this.annualLeaveDateIntervalRepository.save(newDateInterval);
                request.addAnnualLeaveDateInterval(newDateInterval);
            } else {
                //change old one
                boolean isValid = false;
                for (AnnualLeaveDateInterval annualLeaveDateIntervalDB : annualLeaveDateIntervalsFromDB) {
                    if (annualLeaveDateIntervalDB.getId().equals(intervalUpdateModel.getId())) {
                        isValid = true;
                        annualLeaveDateIntervalDB.setStartDate(intervalUpdateModel.getStartDate());
                        annualLeaveDateIntervalDB.setEndDate(intervalUpdateModel.getEndDate());
                        if (!annualLeaveDateIntervalDB.getType().getId().equals(intervalUpdateModel.getAnnualLeaveTypeId())) {
                            AnnualLeaveType newType = this.annualLeaveTypeRepository.findOne(intervalUpdateModel.getAnnualLeaveTypeId());
                            if (newType == null) {
                                FieldError fieldError = new FieldError("annualLeaveType", "annualLeaveType", "error.annualLeaveType.notValidId");
                                throw new CustomFieldError("annualLeaveType", fieldError);
                            }
                            annualLeaveDateIntervalDB.setType(newType);
                            intervalsToDelete.remove(annualLeaveDateIntervalDB);
                            break;
                        }
                    }
                }

                if (!isValid) {
                    FieldError fieldError = new FieldError("annualLeaveDateIntervalDB", "annualLeaveDateIntervalDB", "error.annualLeaveDateIntervalDB.notValidId");
                    throw new CustomFieldError("annualLeaveDateIntervalDB", fieldError);
                }
            }
        }

        for (AnnualLeaveDateInterval intervalToDelete : intervalsToDelete) {
            request.removeAnnualLeaveDateInterval(intervalToDelete);
            intervalToDelete.setAnnualLeaveRequest(null);
            this.annualLeaveDateIntervalRepository.delete(intervalToDelete);
        }

        return true;
    }

    @Override
    public Long getEmployeeIdByRequestId(Long requestId) {
        return this.annualLeaveRequestRepository.getEmployeeIdByRequestId(requestId);
    }

    @Override
    public boolean isEmployeeOwnerOfRequest(Long employeeId, Long requestId) {
        Long employeeFromDB = this.annualLeaveRequestRepository.getEmployeeIdByRequestId(requestId);
        if (employeeId != null && employeeId.equals(employeeFromDB)) {
            return true;
        }

        return false;
    }

    @Override
    public void deleteRequest(Long requestId) {
        this.annualLeaveRequestRepository.delete(requestId);
    }

    @Autowired
    public void setAnnualLeaveDateIntervalRepository(AnnualLeaveDateIntervalRepository annualLeaveDateIntervalRepository) {
        this.annualLeaveDateIntervalRepository = annualLeaveDateIntervalRepository;
    }

    @Autowired
    public void setAnnualLeaveRequestRepository(AnnualLeaveRequestRepository annualLeaveRequestRepository) {
        this.annualLeaveRequestRepository = annualLeaveRequestRepository;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setEmployeeShiftService(EmployeeShiftService employeeShiftService) {
        this.employeeShiftService = employeeShiftService;
    }

    @Autowired
    public void setAnnualLeaveTypeRepository(AnnualLeaveTypeRepository annualLeaveTypeRepository) {
        this.annualLeaveTypeRepository = annualLeaveTypeRepository;
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
    public void setAnnualLeaveDateIntervalService(AnnualLeaveDateIntervalService annualLeaveDateIntervalService) {
        this.annualLeaveDateIntervalService = annualLeaveDateIntervalService;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    private String convertDateTimeToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "";
        }

        int day = dateTime.getDayOfMonth();
        String dayAsString = String.valueOf(day);
        if (day < 10) {
            dayAsString = "0" + dayAsString;
        }

        int month = dateTime.getMonthValue();
        String monthAsString = String.valueOf(month);
        if (month < 10) {
            monthAsString = "0" + monthAsString;
        }

        int minute = dateTime.getMinute();
        String minuteAsString = String.valueOf(minute);
        if (minute < 10) {
            minuteAsString = "0" + minuteAsString;
        }

        int hour = dateTime.getHour();
        String hourAsString = String.valueOf(hour);
        if (hour < 10) {
            hourAsString = "0" + hourAsString;
        }

        return dayAsString + "." + monthAsString + "." + dateTime.getYear() + " / " + hourAsString + ":" + minuteAsString;
    }
}
