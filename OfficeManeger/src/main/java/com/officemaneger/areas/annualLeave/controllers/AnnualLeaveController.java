package com.officemaneger.areas.annualLeave.controllers;

import com.officemaneger.areas.annualLeave.models.bindingModels.AnnualLeaveCreationModel;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewHisCurrent;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewHisUsed;
import com.officemaneger.areas.annualLeave.services.AnnualLeaveService;
import com.officemaneger.areas.annualLeaveType.services.AnnualLeaveTypeService;
import com.officemaneger.areas.employee.models.viewModels.EmployeeAnnualLeavesViewModel;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.configs.errors.CustomFieldError;
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
public class AnnualLeaveController {

    private AnnualLeaveService annualLeaveService;

    private AnnualLeaveTypeService annualLeaveTypeService;

    private UserService userService;

    private EmployeeService employeeService;

    public AnnualLeaveController() {
    }

    @GetMapping("/viewAllAnnualLeaves")
    public String getViewAllAnnualLeavePage(Model model){
        List<String> activeAnnualLeaveTypes = this.annualLeaveTypeService.getActiveAnnualLeaveTypesAsString();
        model.addAttribute("activeAnnualLeaveTypes", activeAnnualLeaveTypes);

        List<EmployeeAnnualLeavesViewModel> employees = this.employeeService.getActiveEmployeeAnnualLeavesViewModels(activeAnnualLeaveTypes);
        model.addAttribute("employees", employees);

        model.addAttribute("view", "annualLeave/viewAllAnnualLeaves");
        model.addAttribute("title", "Отпуск на служителите");

        return "base-layout";
    }

    @GetMapping("/annualLeave/create")
    public String getCreateAnnualLeavePage(Model model){
        model.addAttribute("view", "annualLeave/create-annualLeave");
        model.addAttribute("title", "Добавяне на отпуск");

        return "base-layout";
    }

    @PostMapping("/annualLeave/create")
    public ResponseEntity createAnnualLeave(@Valid @RequestBody AnnualLeaveCreationModel annualLeaveCreationModel, Principal principal){
        Long employeeId = annualLeaveCreationModel.getEmployeeId();
        Long annualLeaveTypeId = annualLeaveCreationModel.getAnnualLeaveTypeId();
        boolean dasHeHasThisTypeOfAnnualLeave = this.annualLeaveService.dasEmployeeHasThisTypeOfAnnualLeave(employeeId, annualLeaveTypeId);
        if (dasHeHasThisTypeOfAnnualLeave) {
            FieldError fieldError = new FieldError("egn", "egn", "error.annualLeave.employeeHasThisAnnualLeave");
            throw new CustomFieldError("egn", fieldError);
        }

        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                this.annualLeaveService.create(annualLeaveCreationModel);
                return new ResponseEntity(HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/annualLeave/update/{annualLeaveId}/{numberOfAnnualLeave}")
    public ResponseEntity updateAnnualLeave(@PathVariable Long annualLeaveId, @PathVariable int numberOfAnnualLeave){
        if (numberOfAnnualLeave < 0) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
        this.annualLeaveService.updateAnnualLeave(annualLeaveId, numberOfAnnualLeave);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/user/annualLeavesInfo")
    public String userViewHisAnnualLeaveInfoPage(Model model, Principal principal){
        String username = principal.getName();
        Long employeeId = this.userService.getAccountOwnerIdByUsername(username);

        model.addAttribute("view", "annualLeave/userViewHisAnnualLeaveInfo");
        model.addAttribute("title", "Информация за отпуските");
        if (employeeId == null) {
            model.addAttribute("currentAnnualLeaves", new ArrayList<>());
            model.addAttribute("usedAnnualLeaves", new ArrayList<>());
            return "base-layout";
        }

        List<AnnualLeaveEmployeeViewHisCurrent> currentAnnualLeaves = this.annualLeaveService.employeeGetHisCurrentAnnualLeaves(employeeId);
        model.addAttribute("currentAnnualLeaves", currentAnnualLeaves);
        List<AnnualLeaveEmployeeViewHisUsed> usedAnnualLeaves = this.annualLeaveService.employeeGetHisUsedAnnualLeaves(employeeId);
        model.addAttribute("usedAnnualLeaves", usedAnnualLeaves);

        return "base-layout";
    }

    @Autowired
    public void setAnnualLeaveService(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setAnnualLeaveTypeService(AnnualLeaveTypeService annualLeaveTypeService) {
        this.annualLeaveTypeService = annualLeaveTypeService;
    }
}

