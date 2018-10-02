package com.officemaneger.areas.workSchedule.controllers;

import com.officemaneger.areas.annualLeaveRequest.models.viewModels.AnnualLeaveRequestViewModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeWorkShiftViewModel;
import com.officemaneger.areas.computers.services.ComputerService;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeInWorkShiftViewModel;
import com.officemaneger.areas.shiftType.services.ShiftTypeService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.areas.workSchedule.models.helpObjects.DateObjectSchedule;
import com.officemaneger.areas.workSchedule.models.viewModels.WorkScheduleViewAllModel;
import com.officemaneger.areas.workSchedule.services.WorkScheduleService;
import com.officemaneger.areas.workScheduleSettings.services.WorkScheduleSettingsService;
import com.officemaneger.areas.workShift.models.heplObjects.DateObject;
import com.officemaneger.areas.workShift.models.viewModels.WorkShiftViewAllModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WorkScheduleController {

    private static final Long DAYS_BEFORE_TODAY = 2L;

    private static final int NUMB_OF_ALL_DAYS= 7;

    private EmployeeService employeeService;

    private WorkScheduleSettingsService workScheduleSettingsService;

    private UserService userService;

    private WorkScheduleService workScheduleService;

    private WorkDayService workDayService;

    private ComputerService computerService;

    public WorkScheduleController() {
    }

    @GetMapping("/boss/workSchedule")
    public String getWorkSchedulePage(Model model, Principal principal, HttpServletRequest httpServletRequest){
        List<LocalDate> startDates = this.getStartDatesForWiewAll();
        List<DateObjectSchedule> dates = this.convertDatesToCustomDateObject(startDates);
        model.addAttribute("dates", dates);

        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        List<WorkScheduleViewAllModel> workShifts = new ArrayList<>();

        Map<String, Map<String, String>> map = new HashMap<>();
        //map.put(1, "first");
        model.addAttribute("map", map);

        switch (roleAsString) {
            case "ROLE_ADMIN":
                Long computerId = 0L;
                if (httpServletRequest.getAttribute("computerId") != null) {
                    try {
                        computerId = (Long) httpServletRequest.getAttribute("computerId");
                    } catch (Exception ex) {
                    }
                }

                if (computerId != null && !computerId.equals(0L)){
                    Long businessUnitId = this.computerService.getBusinessUnitIdByComputerId(computerId);
                    if (businessUnitId != null) {
                        workShifts = this.workScheduleService.bossOrComputerGetWorkScheduleViewAllModels(startDates, businessUnitId);
                    }
                }
                break;
            case "ROLE_BOSS":
                Long bossId = this.userService.getAccountOwnerIdByUsername(username);
                Long businessUnitId = this.employeeService.getBusinessUnitIdByEmployeeId(bossId);
                workShifts = this.workScheduleService.bossOrComputerGetWorkScheduleViewAllModels(startDates, businessUnitId);
                break;
            default:
                break;
        }

        model.addAttribute("workShifts", workShifts);

        model.addAttribute("view", "workSchedule/viewWorkSchedules");
        model.addAttribute("title", "График");

        return "base-layout";
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setWorkScheduleSettingsService(WorkScheduleSettingsService workScheduleSettingsService) {
        this.workScheduleSettingsService = workScheduleSettingsService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setWorkScheduleService(WorkScheduleService workScheduleService) {
        this.workScheduleService = workScheduleService;
    }

    @Autowired
    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    private List<LocalDate> getStartDatesForWiewAll() {
        List<LocalDate> dates = new ArrayList<>();
        LocalDate today = LocalDate.now();
        LocalDate startDate = today.minusDays(DAYS_BEFORE_TODAY);
        for (long i = 0; i < NUMB_OF_ALL_DAYS; i++) {
            LocalDate currentDate = startDate.plusDays(i);
            dates.add(currentDate);
        }

        return dates;
    }

    private List<DateObjectSchedule> convertDatesToCustomDateObject(List<LocalDate> dates) {
        List<DateObjectSchedule> convertedDates = new ArrayList<>();
        for (LocalDate date : dates) {
            convertedDates.add(new DateObjectSchedule(date, this.workDayService.getWorkDayByDate(date)));
        }

        return convertedDates;
    }
}
