package com.officemaneger.areas.annualLeaveRequest.controllers;

import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveWorkShiftViewModel;
import com.officemaneger.areas.annualLeave.services.AnnualLeaveService;
import com.officemaneger.areas.annualLeaveDateInterval.models.bindingModels.AnnualLeaveDateIntervalCreationModel;
import com.officemaneger.areas.annualLeaveDateInterval.models.bindingModels.AnnualLeaveDateIntervalUpdateModel;
import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalViewModel;
import com.officemaneger.areas.annualLeaveRequest.models.bindingModels.AnnualLeaveRequestUpdateModel;
import com.officemaneger.areas.annualLeaveRequest.models.bindingModels.AnnualLeaveRequestUserCreationModel;
import com.officemaneger.areas.annualLeaveRequest.models.viewModels.AnnualLeaveRequestUpdateViewModel;
import com.officemaneger.areas.annualLeaveRequest.services.AnnualLeaveRequestService;
import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.dateInterval.entities.DateInterval;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.configs.errors.CustomFieldError;
import com.officemaneger.utility.DateCalculator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AnnualLeaveRequestController {

    private AnnualLeaveRequestService annualLeaveRequestService;

    private UserService userService;

    private AnnualLeaveService annualLeaveService;

    private BusinessUnitService businessUnitService;

    private EmployeeService employeeService;

    public AnnualLeaveRequestController() {
    }

    @GetMapping("/user/annualLeaveRequest/create")
    public String getCreateAnnualLeaveRequestPage(Model model, Principal principal){
        Long employeeId = this.getEmployeeIdByPrincipal(principal);
        List<AnnualLeaveWorkShiftViewModel> annualLeaves = new ArrayList<>();
        if (employeeId != null) {
            annualLeaves = this.annualLeaveService.getAnnualLeaveWorkShiftViewModel(employeeId);
        }

        model.addAttribute("annualLeaves", annualLeaves);

        model.addAttribute("view", "annualLeaveRequest/createAnnualLeaveRequest");
        model.addAttribute("title", "Заявление за отпуск");

        return "base-layout";
    }

    @PostMapping("/user/annualLeaveRequest/create")
    public ResponseEntity createAnnualLeaveRequest(@Valid @RequestBody AnnualLeaveRequestUserCreationModel annualLeaveRequestModel, Principal principal){
        Long employeeId = this.getEmployeeIdByPrincipal(principal);
        if (employeeId == null) {
            FieldError fieldError = new FieldError("employeeId", "employeeId", "error.annualLeaveRequest.notValidEmployee");
            throw new CustomFieldError("employeeId", fieldError);
        }

        if (annualLeaveRequestModel.getAnnualLeaveDateIntervals().isEmpty()) {
            FieldError fieldError = new FieldError("employeeId", "employeeId", "error.annualLeaveRequest.notValidDateInterval");
            throw new CustomFieldError("employeeId", fieldError);
        }

        List<Long> annualLeaveTypeIds = new ArrayList<>();
        List<DateInterval> dateIntervals = new ArrayList<>();
        for (AnnualLeaveDateIntervalCreationModel annualLeaveDateInterval : annualLeaveRequestModel.getAnnualLeaveDateIntervals()) {
            Long annualLeaveTypeId = annualLeaveDateInterval.getAnnualLeaveTypeId();
            if (annualLeaveTypeId < 1) {
                FieldError fieldError = new FieldError("annualLeaveTypeId", "annualLeaveTypeId", "error.annualLeaveDateInterval.annualLeaveTypeIdNull");
                throw new CustomFieldError("annualLeaveTypeId", fieldError);
            }

            if (annualLeaveDateInterval.getStartDate().isAfter(annualLeaveDateInterval.getEndDate())) {
                FieldError fieldError = new FieldError("dates", "dates", "error.annualLeaveDateInterval.startDateIsAfterEndDate");
                throw new CustomFieldError("dates", fieldError);
            }

            if (annualLeaveTypeIds.contains(annualLeaveTypeId)) {
                FieldError fieldError = new FieldError("annualLeaveTypeId", "annualLeaveTypeId", "error.annualLeaveRequest.duplicateAnnualLeaveTypeId");
                throw new CustomFieldError("annualLeaveTypeId", fieldError);
            }

            DateInterval newDateInterval = new DateInterval(annualLeaveDateInterval.getStartDate(), annualLeaveDateInterval.getEndDate());
            for (DateInterval dateInterval : dateIntervals) {
                if (DateCalculator.doesHaveDateIntervalsConflict(dateInterval, newDateInterval)) {
                    FieldError fieldError = new FieldError("dateIntervals", "dateIntervals", "error.annualLeaveRequest.dateIntervalsConflict");
                    throw new CustomFieldError("dateIntervals", fieldError);
                }
            }

            dateIntervals.add(newDateInterval);
            annualLeaveTypeIds.add(annualLeaveTypeId);
        }

        annualLeaveRequestModel.setEmployeeId(employeeId);
        boolean isRequestSaved = this.annualLeaveRequestService.userCreateRequest(annualLeaveRequestModel);
        if (isRequestSaved) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/user/annualLeaveRequest/update/{requestId}")
    public String getUpdateAnnualLeaveRequestPage(@PathVariable long requestId, Model model, Principal principal){
        Long employeeId = this.getEmployeeIdByPrincipal(principal);
        AnnualLeaveRequestUpdateViewModel request = this.annualLeaveRequestService.employeeGetUpdateRequestModel(employeeId, requestId);
        model.addAttribute("request", request);

        List<AnnualLeaveWorkShiftViewModel> annualLeaves = new ArrayList<>();
        if (employeeId != null) {
            annualLeaves = this.annualLeaveService.getAnnualLeaveWorkShiftViewModel(employeeId);
        }

        model.addAttribute("annualLeaves", annualLeaves);

        model.addAttribute("view", "annualLeaveRequest/updateAnnualLeaveRequest");
        model.addAttribute("title", "Редактиране на заявление за отпуск");

        return "base-layout";
    }

    @PostMapping("/user/annualLeaveRequest/update")
    public ResponseEntity updateAnnualLeaveRequest(@Valid @RequestBody AnnualLeaveRequestUpdateModel annualLeaveRequestModel, Principal principal){
        Long employeeId = this.getEmployeeIdByPrincipal(principal);
        if (employeeId == null) {
            FieldError fieldError = new FieldError("employeeId", "employeeId", "error.annualLeaveRequest.notValidEmployee");
            throw new CustomFieldError("employeeId", fieldError);
        }

        boolean isHisRequest = this.annualLeaveRequestService.isEmployeeOwnerOfRequest(employeeId, annualLeaveRequestModel.getId());
        if (!isHisRequest) {
            FieldError fieldError = new FieldError("requestId", "requestId", "error.annualLeaveRequest.notHisRequest");
            throw new CustomFieldError("requestId", fieldError);
        }

        if (annualLeaveRequestModel.getAnnualLeaveDateIntervals().isEmpty()) {
            FieldError fieldError = new FieldError("employeeId", "employeeId", "error.annualLeaveRequest.notValidDateInterval");
            throw new CustomFieldError("employeeId", fieldError);
        }

        List<Long> annualLeaveTypeIds = new ArrayList<>();
        List<DateInterval> dateIntervals = new ArrayList<>();
        for (AnnualLeaveDateIntervalUpdateModel annualLeaveDateInterval : annualLeaveRequestModel.getAnnualLeaveDateIntervals()) {
            Long annualLeaveTypeId = annualLeaveDateInterval.getAnnualLeaveTypeId();
            if (annualLeaveTypeId < 1) {
                FieldError fieldError = new FieldError("annualLeaveTypeId", "annualLeaveTypeId", "error.annualLeaveDateInterval.annualLeaveTypeIdNull");
                throw new CustomFieldError("annualLeaveTypeId", fieldError);
            }

            if (annualLeaveDateInterval.getStartDate().isAfter(annualLeaveDateInterval.getEndDate())) {
                FieldError fieldError = new FieldError("dates", "dates", "error.annualLeaveDateInterval.startDateIsAfterEndDate");
                throw new CustomFieldError("dates", fieldError);
            }

            if (annualLeaveTypeIds.contains(annualLeaveTypeId)) {
                FieldError fieldError = new FieldError("annualLeaveTypeId", "annualLeaveTypeId", "error.annualLeaveRequest.duplicateAnnualLeaveTypeId");
                throw new CustomFieldError("annualLeaveTypeId", fieldError);
            }

            DateInterval newDateInterval = new DateInterval(annualLeaveDateInterval.getStartDate(), annualLeaveDateInterval.getEndDate());
            for (DateInterval dateInterval : dateIntervals) {
                if (DateCalculator.doesHaveDateIntervalsConflict(dateInterval, newDateInterval)) {
                    FieldError fieldError = new FieldError("dateIntervals", "dateIntervals", "error.annualLeaveRequest.dateIntervalsConflict");
                    throw new CustomFieldError("dateIntervals", fieldError);
                }
            }

            dateIntervals.add(newDateInterval);
            annualLeaveTypeIds.add(annualLeaveTypeId);
        }

        annualLeaveRequestModel.setEmployeeId(employeeId);
        boolean isRequestUpdated = this.annualLeaveRequestService.userUpdateRequest(annualLeaveRequestModel);
        if (isRequestUpdated) {
            return new ResponseEntity(HttpStatus.OK);
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/allUsers/deleteAnnualLeaveRequest/{requestId}")
    public ResponseEntity deleteAnnualLeaveRequest(@PathVariable long requestId, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        if (roleAsString.equals("ROLE_USER")) {
            Long employeeId = this.userService.getAccountOwnerIdByUsername(username);
            Long employeeFromRequest = this.annualLeaveRequestService.getEmployeeIdByRequestId(requestId);
            if (employeeId.equals(employeeFromRequest)) {
                this.annualLeaveRequestService.deleteRequest(requestId);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else if (roleAsString.equals("ROLE_BOSS")) {
            Long employeeFromRequest = this.annualLeaveRequestService.getEmployeeIdByRequestId(requestId);
            if (employeeFromRequest == null) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

            Long unitIdByRequest = this.employeeService.getBusinessUnitIdByEmployeeId(employeeFromRequest);
            Long currentBossId = this.userService.getAccountOwnerIdByUsername(username);
            Long unitIdByBoss = this.employeeService.getBusinessUnitIdByEmployeeId(currentBossId);
            if (unitIdByRequest.equals(unitIdByBoss)) {
                this.annualLeaveRequestService.deleteRequest(requestId);
                return new ResponseEntity(HttpStatus.OK);
            } else {
                //check main unit
                Long mainUnitId = this.businessUnitService.getMainUnitIdByUnitId(unitIdByRequest);
                while (mainUnitId != null) {
                    if (unitIdByBoss.equals(mainUnitId)) {
                        this.annualLeaveRequestService.deleteRequest(requestId);
                        return new ResponseEntity(HttpStatus.OK);
                    }

                    mainUnitId = this.businessUnitService.getMainUnitIdByUnitId(mainUnitId);
                }

                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else if (roleAsString.equals("ROLE_ADMIN")) {
            this.annualLeaveRequestService.deleteRequest(requestId);
            return new ResponseEntity(HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/boss/executeEmployeeRequest/{requestId}")
    public ResponseEntity<List<AnnualLeaveDateIntervalViewModel>> executeEmployeeRequest(@PathVariable long requestId, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        List<AnnualLeaveDateIntervalViewModel> viewModels = new ArrayList<>();
        if (roleAsString.equals("ROLE_BOSS")) {
            Long employeeFromRequest = this.annualLeaveRequestService.getEmployeeIdByRequestId(requestId);
            if (employeeFromRequest == null) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

            Long unitIdByRequest = this.employeeService.getBusinessUnitIdByEmployeeId(employeeFromRequest);
            Long currentBossId = this.userService.getAccountOwnerIdByUsername(username);
            Long unitIdByBoss = this.employeeService.getBusinessUnitIdByEmployeeId(currentBossId);
            if (unitIdByRequest.equals(unitIdByBoss)) {
                viewModels = this.annualLeaveRequestService.executeEmployeeRequest(requestId);
                if (viewModels == null) {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                } else {
                    return new ResponseEntity(viewModels, HttpStatus.OK);
                }
            } else {
                //check main unit
                Long mainUnitId = this.businessUnitService.getMainUnitIdByUnitId(unitIdByRequest);
                while (mainUnitId != null) {
                    if (unitIdByBoss.equals(mainUnitId)) {
                        viewModels = this.annualLeaveRequestService.executeEmployeeRequest(requestId);
                        if (viewModels == null) {
                            return new ResponseEntity(HttpStatus.BAD_REQUEST);
                        } else {
                            return new ResponseEntity(viewModels, HttpStatus.OK);
                        }
                    }

                    mainUnitId = this.businessUnitService.getMainUnitIdByUnitId(mainUnitId);
                }

                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } else if (roleAsString.equals("ROLE_ADMIN")) {
            viewModels = this.annualLeaveRequestService.executeEmployeeRequest(requestId);
            if (viewModels == null) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity(viewModels, HttpStatus.OK);
            }
        } else {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public void setAnnualLeaveRequestService(AnnualLeaveRequestService annualLeaveRequestService) {
        this.annualLeaveRequestService = annualLeaveRequestService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @Autowired
    public void setAnnualLeaveService(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    private Long getEmployeeIdByPrincipal(Principal principal) {
        if (principal == null) {
            return null;
        }

        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        if (roleAsString.equals("ROLE_USER")) {
            return this.userService.getAccountOwnerIdByUsername(username);
        }

        return null;
    }
}
