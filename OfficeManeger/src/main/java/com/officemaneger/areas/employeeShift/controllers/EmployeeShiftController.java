package com.officemaneger.areas.employeeShift.controllers;

import com.officemaneger.areas.annualLeave.services.AnnualLeaveService;
import com.officemaneger.areas.employeeShift.models.bindingModels.EmployeeShiftAddMultiple;
import com.officemaneger.areas.employeeShift.models.bindingModels.EmployeeShiftInWorkShiftUpdateModel;
import com.officemaneger.areas.employeeShift.services.EmployeeShiftService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class EmployeeShiftController {

    private EmployeeShiftService employeeShiftService;

    private UserService userService;

    private WorkDayService workDayService;

    private AnnualLeaveService annualLeaveService;

    public EmployeeShiftController() {
    }

    @PutMapping("/boss/updateEmployeeShift")
    public ResponseEntity updateEmployeeShift(@Valid @RequestBody EmployeeShiftInWorkShiftUpdateModel employeeShift, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        boolean isUpdated = false;
        switch (roleAsString) {
            case "ROLE_ADMIN":
                isUpdated = this.employeeShiftService.updateEmployeeShiftFromWorkShift(employeeShift);
                break;
            case "ROLE_BOSS":
                boolean hasAuthority = this.userService.bossHasAuthorityToChangeEmployee(username, employeeShift.getEmployeeId());
                if (!hasAuthority) {
                    FieldError fieldError = new FieldError("numbOfAnnualLeave", "numbOfAnnualLeave", "error.noAuthority");
                    throw new CustomFieldError("numbOfAnnualLeave", fieldError);
                }

                isUpdated = this.employeeShiftService.updateEmployeeShiftFromWorkShift(employeeShift);
                break;
            default:
                isUpdated = false;
                break;
        }

        if (isUpdated) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/boss/addMultipleEmployeeShifts")
    public ResponseEntity updateOrAddMultipleEmployeeShifts(@Valid @RequestBody EmployeeShiftAddMultiple employeeShiftAddMultiple, Principal principal){
        if (employeeShiftAddMultiple.getIsAnnualLeave()) {
            Long numbOfAnnualLeave = Long.valueOf(employeeShiftAddMultiple.getNumbOfAnnualLeave());
            Long numbOfWorkingDays = this.workDayService.getNumbOfWorkingDaysBetweenDates(employeeShiftAddMultiple.getStartDate(), employeeShiftAddMultiple.getEndDate());
            if (!numbOfAnnualLeave.equals(numbOfWorkingDays)) {
                FieldError fieldError = new FieldError("numbOfAnnualLeave", "numbOfAnnualLeave", "error.employeeShiftAddMultipleAnnualLeave.notValidDays");
                throw new CustomFieldError("numbOfAnnualLeave", fieldError);
            }

            boolean hasEmployeeThisNumbOfAnnualLeave = this.annualLeaveService.hasEmployeeThisNumbOfAnnualLeave(employeeShiftAddMultiple.getEmployeeId(), employeeShiftAddMultiple.getShiftTypeId(), employeeShiftAddMultiple.getNumbOfAnnualLeave());
            if (!hasEmployeeThisNumbOfAnnualLeave) {
                FieldError fieldError = new FieldError("numbOfAnnualLeave", "numbOfAnnualLeave", "error.employeeShiftAddMultipleAnnualLeave.notValidNumbOfAnnualLeave");
                throw new CustomFieldError("numbOfAnnualLeave", fieldError);
            }
        }

        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        boolean isUpdated = false;
        switch (roleAsString) {
            case "ROLE_ADMIN":
                isUpdated = this.employeeShiftService.updateOrAddMultipleEmployeeShiftsFromWorkShift(employeeShiftAddMultiple);
                break;
            case "ROLE_BOSS":
                boolean hasAuthority = this.userService.bossHasAuthorityToChangeEmployee(username, employeeShiftAddMultiple.getEmployeeId());
                if (!hasAuthority) {
                    FieldError fieldError = new FieldError("numbOfAnnualLeave", "numbOfAnnualLeave", "error.noAuthority");
                    throw new CustomFieldError("numbOfAnnualLeave", fieldError);
                }

                isUpdated = this.employeeShiftService.updateOrAddMultipleEmployeeShiftsFromWorkShift(employeeShiftAddMultiple);
                break;
            default:
                isUpdated = false;
                break;
        }

        if (isUpdated) {
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public void setEmployeeShiftService(EmployeeShiftService employeeShiftService) {
        this.employeeShiftService = employeeShiftService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }

    @Autowired
    public void setAnnualLeaveService(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }
}
