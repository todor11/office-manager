package com.officemaneger.areas.workShift.controllers;

import com.officemaneger.areas.annualLeaveRequest.models.viewModels.AnnualLeaveRequestViewModel;
import com.officemaneger.areas.annualLeaveRequest.services.AnnualLeaveRequestService;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeWorkShiftViewModel;
import com.officemaneger.areas.annualLeaveType.services.AnnualLeaveTypeService;
import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeInWorkShiftViewModel;
import com.officemaneger.areas.shiftType.services.ShiftTypeService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.areas.workShift.models.bindingModels.DateIntervalAsk;
import com.officemaneger.areas.workShift.models.heplObjects.DateObject;
import com.officemaneger.areas.workShift.models.viewModels.WorkShiftViewAllModel;
import com.officemaneger.areas.workShift.services.WorkShiftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkShiftController {

    private static final Long DAYS_BEFORE_TODAY = 7L;

    private static final int NUMB_OF_ALL_DAYS= 16;

    private WorkShiftService workShiftService;

    private WorkDayService workDayService;

    private ShiftTypeService shiftTypeService;

    private AnnualLeaveTypeService annualLeaveTypeService;

    private UserService userService;

    private AnnualLeaveRequestService annualLeaveRequestService;

    private EmployeeService employeeService;

    public WorkShiftController() {
    }

    @GetMapping("/boss/workShifts")
    public String getWorkShiftsPage(Model model, Principal principal){
        List<LocalDate> startDates = this.getStartDatesForWiewAll();
        List<DateObject> dates = this.convertDatesToCustomDateObject(startDates);
        model.addAttribute("dates", dates);

        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        List<WorkShiftViewAllModel> workShifts = new ArrayList<>();
        List<AnnualLeaveRequestViewModel> requests = new ArrayList<>();
        Boolean hasRequests = false;

        List<ShiftTypeInWorkShiftViewModel> regularShiftTypes = new ArrayList<>();
        switch (roleAsString) {
            case "ROLE_ADMIN":
                workShifts = this.workShiftService.getWorkShiftViewAllModels(startDates);
                regularShiftTypes = this.shiftTypeService.getRegularAndActiveShiftTypes();
                break;
            case "ROLE_BOSS":
                Long bossId = this.userService.getAccountOwnerIdByUsername(username);
                workShifts = this.workShiftService.getWorkShiftViewAllModelsByBossId(startDates, bossId);
                requests = this.annualLeaveRequestService.bossGetRequests(bossId);
                if (requests == null) {
                    requests = new ArrayList<>();
                }

                if (!requests.isEmpty()) {
                    hasRequests = true;
                }

                Long businessUnitId = this.employeeService.getBusinessUnitIdByEmployeeId(bossId);
                regularShiftTypes = this.shiftTypeService.getRegularAndActiveShiftTypesByBusinessUnit(businessUnitId);

                break;
            default:
                break;
        }

        model.addAttribute("hasRequests", hasRequests);
        model.addAttribute("requests", requests);
        model.addAttribute("workShifts", workShifts);

        //List<ShiftTypeInWorkShiftViewModel> regularShiftTypes = this.shiftTypeService.getRegularAndActiveShiftTypes();
        model.addAttribute("regularShiftTypes", regularShiftTypes);

        List<ShiftTypeInWorkShiftViewModel> notRegularShiftTypes = this.shiftTypeService.getNotRegularAndActiveShiftTypes();
        model.addAttribute("notRegularShiftTypes", notRegularShiftTypes);

        List<ShiftTypeInWorkShiftViewModel> allShiftTypes = new ArrayList<>();
        allShiftTypes.addAll(regularShiftTypes);
        allShiftTypes.addAll(notRegularShiftTypes);
        model.addAttribute("allShiftTypes", allShiftTypes);

        List<AnnualLeaveTypeWorkShiftViewModel> annualLeaveTypeViewModels = this.annualLeaveTypeService.getAllActiveAnnualLeaveTypesInWorkShift();
        model.addAttribute("annualLeaveTypeViewModels", annualLeaveTypeViewModels);

        model.addAttribute("view", "workShift/viewWorkShifts");
        model.addAttribute("title", "Разход");

        return "base-layout";
    }

    @GetMapping("/all/anonymousViewWorkShifts")
    public String anonymousViewWorkShiftsPage(Model model, HttpServletRequest httpServletRequest, Principal principal){
        boolean isAdmin = false;
        if (principal != null) {
            String username = principal.getName();
            String roleAsString = this.userService.getUserRoleAsString(username);
            if (roleAsString.equals("ROLE_ADMIN")) {
                isAdmin = true;
            }
        }

        List<LocalDate> startDates = this.getStartDatesForWiewAll();
        List<DateObject> dates = this.convertDatesToCustomDateObject(startDates);
        model.addAttribute("dates", dates);

        List<WorkShiftViewAllModel> workShifts = new ArrayList<>();
        if (isAdmin) {
             workShifts = this.workShiftService.getWorkShiftViewAllModels(startDates);
        } else {
            Long computerId = 0L;
            if (httpServletRequest.getAttribute("computerId") != null) {
                try {
                    computerId = (Long) httpServletRequest.getAttribute("computerId");
                } catch (Exception ex) {
                }
                if (!computerId.equals(0L)){
                    workShifts = this.workShiftService.getWorkShiftViewAllModelsByComputerId(startDates, computerId);
                }
            }
        }
        model.addAttribute("workShifts", workShifts);

        List<ShiftTypeInWorkShiftViewModel> regularShiftTypes = this.shiftTypeService.getRegularAndActiveShiftTypes();
        model.addAttribute("regularShiftTypes", regularShiftTypes);

        List<ShiftTypeInWorkShiftViewModel> notRegularShiftTypes = this.shiftTypeService.getNotRegularAndActiveShiftTypes();
        model.addAttribute("notRegularShiftTypes", notRegularShiftTypes);

        List<ShiftTypeInWorkShiftViewModel> allShiftTypes = new ArrayList<>();
        allShiftTypes.addAll(regularShiftTypes);
        allShiftTypes.addAll(notRegularShiftTypes);
        model.addAttribute("allShiftTypes", allShiftTypes);

        List<AnnualLeaveTypeWorkShiftViewModel> annualLeaveTypeViewModels = this.annualLeaveTypeService.getAllActiveAnnualLeaveTypesInWorkShift();
        model.addAttribute("annualLeaveTypeViewModels", annualLeaveTypeViewModels);

        model.addAttribute("view", "workShift/anonViewWorkShifts");
        model.addAttribute("title", "Разход");

        return "base-layout";
    }

    @PostMapping("/all/workShifts/getNewDates")
    public ResponseEntity<List<DateObject>> getNewWorkShiftsDates(@Valid @RequestBody DateIntervalAsk dateInterval){
        List<LocalDate> datesObj = new ArrayList<>();
        LocalDate startDate = dateInterval.getStartDate();
        LocalDate endDate = dateInterval.getEndDate();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        while (!startDate.isAfter(endDate)) {
            datesObj.add(startDate);
            startDate = startDate.plusDays(1L);
        }

        List<DateObject> dates = this.convertDatesToCustomDateObject(datesObj);

        return new ResponseEntity<>(dates, HttpStatus.OK);
    }

    @PostMapping("/all/getWorkShiftsByDates")
    public ResponseEntity<List<WorkShiftViewAllModel>> getWorkShiftsByDates(@Valid @RequestBody DateIntervalAsk dateInterval, HttpServletRequest httpServletRequest, Principal principal){
        List<LocalDate> datesObj = new ArrayList<>();
        LocalDate startDate = dateInterval.getStartDate();
        LocalDate endDate = dateInterval.getEndDate();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        while (!startDate.isAfter(endDate)) {
            datesObj.add(startDate);
            startDate = startDate.plusDays(1L);
        }

        String roleAsString = "anonymous";
        String username = null;
        if (principal != null) {
            username = principal.getName();
            roleAsString = this.userService.getUserRoleAsString(username);
        }

        List<WorkShiftViewAllModel> workShifts = null;
        switch (roleAsString) {
            case "ROLE_ADMIN":
                workShifts = this.workShiftService.getWorkShiftViewAllModels(datesObj);
                return new ResponseEntity<>(workShifts, HttpStatus.OK);
            case "ROLE_BOSS":
                Long bossId = this.userService.getAccountOwnerIdByUsername(username);

                workShifts = this.workShiftService.getWorkShiftViewAllModelsByBossId(datesObj, bossId);
                return new ResponseEntity<>(workShifts, HttpStatus.OK);
            case "anonymous":
                Long computerId = 0L;
                if (httpServletRequest.getAttribute("computerId") != null) {
                    try {
                        computerId = (Long) httpServletRequest.getAttribute("computerId");
                    } catch (Exception ex) {
                    }
                    if (!computerId.equals(0L)){
                        workShifts = this.workShiftService.getWorkShiftViewAllModelsByComputerId(datesObj, computerId);
                    }
                }
                return new ResponseEntity<>(workShifts, HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public void setWorkShiftService(WorkShiftService workShiftService) {
        this.workShiftService = workShiftService;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    @Autowired
    public void setShiftTypeService(ShiftTypeService shiftTypeService) {
        this.shiftTypeService = shiftTypeService;
    }

    @Autowired
    public void setAnnualLeaveTypeService(AnnualLeaveTypeService annualLeaveTypeService) {
        this.annualLeaveTypeService = annualLeaveTypeService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setAnnualLeaveRequestService(AnnualLeaveRequestService annualLeaveRequestService) {
        this.annualLeaveRequestService = annualLeaveRequestService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private List<LocalDate> getStartDatesForWiewAll() {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(DAYS_BEFORE_TODAY + 1);
        for (long i = 0; i < NUMB_OF_ALL_DAYS; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            dates.add(currentDate);
        }

        return dates;
    }

    private List<DateObject> convertDatesToCustomDateObject(List<LocalDate> dates) {
        List<DateObject> convertedDates = new ArrayList<>();
        for (LocalDate date : dates) {
            LocalDate nextDate = date.plusDays(1L);
            convertedDates.add(new DateObject(this.workDayService.getWorkDayByDate(date), this.workDayService.getWorkDayByDate(nextDate)));
        }

        return convertedDates;
    }
}
