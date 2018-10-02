package com.officemaneger.areas.workDay.controllers;

import com.officemaneger.areas.workDay.models.bindingModels.AddHolidayModel;
import com.officemaneger.areas.workDay.models.bindingModels.AddWorkDayModel;
import com.officemaneger.areas.workDay.models.viewModels.HolidayViewModel;
import com.officemaneger.areas.workDay.models.viewModels.WorkDayViewModel;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.areas.workShift.models.bindingModels.DateIntervalAsk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkDayController {

    private WorkDayService workDayService;

    public WorkDayController() {
    }

    @GetMapping("/workdays")
    public String getAllWorkDaysPage(Model model){
        List<WorkDayViewModel> workDays = this.workDayService.getAllExtraWorkingDays();
        List<HolidayViewModel> holidays = this.workDayService.getAllHolidayDays();
        model.addAttribute("workDays", workDays);
        model.addAttribute("holidays", holidays);

        model.addAttribute("title", "Workdays");
        model.addAttribute("view", "workDay/workdays");

        return "base-layout";
    }

    @PostMapping("/workdays/addWorkday")
    public ResponseEntity<WorkDayViewModel> saveNewWorkDay(@Validated @RequestBody AddWorkDayModel addWorkDayModel){
        WorkDayViewModel workDayViewModel = this.workDayService.create(addWorkDayModel);
        return new ResponseEntity(workDayViewModel, HttpStatus.OK);
    }

    @PostMapping("/workdays/addHoliday")
    public ResponseEntity<HolidayViewModel> saveNewHoliday(@Validated @RequestBody AddHolidayModel addHolidayModel){
        HolidayViewModel holidayViewModel = this.workDayService.create(addHolidayModel);
        return new ResponseEntity(holidayViewModel, HttpStatus.OK);
    }

    @DeleteMapping("/workdays/delete/{workdayId}")
    public ResponseEntity deleteWorkday(@PathVariable long workdayId){
        this.workDayService.delete(workdayId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("/all/getWorkingDaysInDateInterval")
    public ResponseEntity<Long> getWorkShiftsByDates(@Valid @RequestBody DateIntervalAsk dateInterval){
        LocalDate startDate = dateInterval.getStartDate();
        LocalDate endDate = dateInterval.getEndDate();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        Long workdays = this.workDayService.getNumbOfWorkingDaysBetweenDates(startDate, endDate);
        return new ResponseEntity<>(workdays, HttpStatus.OK);
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }
}

