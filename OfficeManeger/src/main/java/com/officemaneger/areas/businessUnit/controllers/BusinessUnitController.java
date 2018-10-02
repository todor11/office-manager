package com.officemaneger.areas.businessUnit.controllers;

import com.officemaneger.areas.businessUnit.models.bindingModels.BusinessUnitCreationModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitEditViewModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitNameViewModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitViewModel;
import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
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
import java.util.List;

@Controller
public class BusinessUnitController {

    private BusinessUnitService businessUnitService;

    private UserService userService;

    public BusinessUnitController() {
    }

    @GetMapping("/businessUnit")
    public String getBusinessUnitsPage(Model model){
        List<BusinessUnitViewModel> businessUnits = this.businessUnitService.getAllBusinessUnitViewModels();
        model.addAttribute("businessUnits", businessUnits);
        model.addAttribute("title", "Структорни звена");
        model.addAttribute("view", "businessUnit/businessUnits");

        return "base-layout";
    }

    @DeleteMapping("/businessUnit/delete/{businessUnitId}")
    public ResponseEntity delete(@PathVariable long businessUnitId){
        this.businessUnitService.delete(businessUnitId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/businessUnit/create")
    public String getCreateBusinessUnit(Model model){
        model.addAttribute("title", "Създаване на звено");
        model.addAttribute("buttonName", "Създай");
        model.addAttribute("buttonEvent", "saveNew");
        model.addAttribute("view", "businessUnit/create-businessUnit");

        return "base-layout";
    }

    @PostMapping("/businessUnit/create")
    public ResponseEntity createBusinessUnit(@Valid @RequestBody BusinessUnitCreationModel businessUnitCreationModel){
        boolean isNameOccupied = this.businessUnitService.isNameOccupied(businessUnitCreationModel.getUnitName());
        if (isNameOccupied) {
            FieldError fieldError = new FieldError("unitName", "unitName", "error.businessUnitUnitName");
            throw new CustomFieldError("fullName", fieldError);
        }

        if (businessUnitCreationModel.getEmployeesPositions().size() != businessUnitCreationModel.getEmployeesIds().size()) {
            FieldError fieldError = new FieldError("employeesPositions", "employeesPositions", "error.businessUnitEmployeesPositions");
            throw new CustomFieldError("employeesPositions", fieldError);
        }

        this.businessUnitService.create(businessUnitCreationModel);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/businessUnit/checkName/{newUnitName}")
    public ResponseEntity validateUnitName(@PathVariable String newUnitName){
        boolean isNameOccupied = this.businessUnitService.isNameOccupied(newUnitName);
        if (isNameOccupied) {
            FieldError fieldError = new FieldError("unitName", "unitName", "error.businessUnitUnitName");
            throw new CustomFieldError("fullName", fieldError);
        }
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping("/businessUnit/view/{unitId}")
    public String getViewBusinessUnit(@PathVariable long unitId, Model model) {
        BusinessUnitEditViewModel businessUnit = this.businessUnitService.getBusinessUnitEditViewModel(unitId);
        model.addAttribute("businessUnit", businessUnit);
        model.addAttribute("title", "Структорно звено");
        model.addAttribute("view", "businessUnit/view-businessUnit");

        return "base-layout";
    }

    @GetMapping("/businessUnit/edit/{unitId}")
    public String getEditBusinessUnitPage(@PathVariable long unitId, Model model) {
        model.addAttribute("title", "Редактиране на звено");
        model.addAttribute("view", "businessUnit/edit-businessUnit");
        //model.addAttribute("buttonEvent", "update");

        return "base-layout";
    }

    @GetMapping("/businessUnit/getEditBusinessUnit/{unitId}")
    public ResponseEntity<BusinessUnitEditViewModel> getEditBusinessUnit(@PathVariable long unitId, Principal principal) {
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                BusinessUnitEditViewModel viewModel = this.businessUnitService.getBusinessUnitEditViewModel(unitId);
                return new ResponseEntity(viewModel , HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/businessUnit/getAllBusinessUnits")
    public ResponseEntity<List<BusinessUnitNameViewModel>> getAllBusinessUnits(Principal principal) {
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                List<BusinessUnitNameViewModel> viewModels = this.businessUnitService.getAllBusinessUnitNameViewModels();
                return new ResponseEntity(viewModels , HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/businessUnit/updateUnitName/{unitId}/{newUnitName}")
    public ResponseEntity updateUnit(@PathVariable long unitId,@PathVariable String newUnitName) {
        boolean isNameOccupied = this.businessUnitService.isNameOccupied(newUnitName, unitId);
        if (isNameOccupied) {
            FieldError fieldError = new FieldError("unitName", "unitName", "error.businessUnitUnitName");
            throw new CustomFieldError("fullName", fieldError);
        }

        this.businessUnitService.updateUnitName(unitId, newUnitName);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/businessUnit/updateMainUnit/{unitId}/{mainUnitId}")
    public ResponseEntity updateUnit(@PathVariable Long unitId,@PathVariable Long mainUnitId) {
        if (unitId == null || unitId == mainUnitId) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }

        this.businessUnitService.updateMainUnit(unitId, mainUnitId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
}

