package com.officemaneger.areas.workScheduleSettings.controllers;

import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.areas.workScheduleSettings.models.bindingModels.WorkScheduleSettingsUpdateModel;
import com.officemaneger.areas.workScheduleSettings.models.viewModels.WorkScheduleSettingsViewModel;
import com.officemaneger.areas.workScheduleSettings.services.WorkScheduleSettingsService;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class WorkScheduleSettingsController {

    private WorkScheduleSettingsService workScheduleSettingsService;

    private UserService userService;

    private EmployeeService employeeService;

    private BusinessUnitService businessUnitService;

    public WorkScheduleSettingsController() {
    }

    @GetMapping("/boss/editWorkScheduleSettings")
    public String getEditWorkScheduleSettingsPage(Model model, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_BOSS":
                Long bossId = this.userService.getAccountOwnerIdByUsername(username);
                Long businessUnitId = this.employeeService.getBusinessUnitIdByEmployeeId(bossId);
                WorkScheduleSettingsViewModel settings = this.workScheduleSettingsService.getScheduleSettings(businessUnitId);
                if (settings == null) {
                    model.addAttribute("title", "Грешка");
                    model.addAttribute("view", "unauthorized");
                    break;
                }
                model.addAttribute("settings", settings);

                model.addAttribute("title", "Редактиране настройките на графика");
                model.addAttribute("view", "workScheduleSettings/workScheduleSettingsEdit");
                break;
            default:
                model.addAttribute("title", "Отказан достъп");
                model.addAttribute("view", "unauthorized");
        }

        return "base-layout";
    }

    @PostMapping("/boss/workScheduleSettingsUpdate")
    public ResponseEntity workScheduleSettingsUpdate(@Valid @RequestBody WorkScheduleSettingsUpdateModel settingsUpdateModel, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_BOSS":
                Long bossId = this.userService.getAccountOwnerIdByUsername(username);
                Long businessUnitId = this.employeeService.getBusinessUnitIdByEmployeeId(bossId);
                Long businessUnitBySettingsId = this.workScheduleSettingsService.getBusinessUnitIdBySettings(settingsUpdateModel.getId());
                if (!businessUnitId.equals(businessUnitBySettingsId)) {
                    FieldError fieldError = new FieldError("workScheduleSettingsUpdate", "workScheduleSettingsUpdate", "error.workScheduleSettingsUpdate.businessUnitId");
                    throw new CustomFieldError("workScheduleSettingsUpdate", fieldError);
                }

                if (settingsUpdateModel.getNumbOfEmployeesInShift() < 1) {
                    FieldError fieldError = new FieldError("workScheduleSettingsUpdate", "workScheduleSettingsUpdate", "error.workScheduleSettingsUpdate.invalidNumbOfEmployees");
                    throw new CustomFieldError("workScheduleSettingsUpdate", fieldError);
                }

                boolean isUpdated = this.workScheduleSettingsService.updateSettings(settingsUpdateModel);
                if (isUpdated) {
                    return new ResponseEntity(HttpStatus.OK);
                }
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
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
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }
}
