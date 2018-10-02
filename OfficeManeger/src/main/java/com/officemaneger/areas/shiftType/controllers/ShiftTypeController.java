package com.officemaneger.areas.shiftType.controllers;

import com.officemaneger.areas.shiftType.models.bindingModels.ShiftTypeCreationModel;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeViewModel;
import com.officemaneger.areas.shiftType.services.ShiftTypeService;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupShortViewModel;
import com.officemaneger.areas.shiftTypeGroup.services.ShiftTypeGroupService;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
public class ShiftTypeController {

    private ShiftTypeService shiftTypeService;

    private ShiftTypeGroupService shiftTypeGroupService;

    public ShiftTypeController() {
    }

    @GetMapping("/shiftTypes")
    public String getShiftTypesPage(Model model){
        List<ShiftTypeViewModel> shiftTypes = this.shiftTypeService.getAllActiveShiftTypes();
        model.addAttribute("shiftTypes", shiftTypes);
        List<ShiftTypeGroupShortViewModel> shiftTypeGroups = this.shiftTypeGroupService.getAllGroups();
        model.addAttribute("shiftTypeGroups", shiftTypeGroups);

        model.addAttribute("title", "Работни смени");
        model.addAttribute("view", "shiftType/shiftTypes");

        return "base-layout";
    }

    @PostMapping("/shiftTypes/add")
    public ResponseEntity<ShiftTypeViewModel> saveShiftType(@Valid @RequestBody ShiftTypeCreationModel shiftTypeCreationModel){
        boolean isFullNameOccupied = this.shiftTypeService.isFullNameOccupied(shiftTypeCreationModel.getFullName());
        if (isFullNameOccupied) {
            FieldError fieldError = new FieldError("fullName", "fullName", "error.annualLeaveTypeFullName");
            throw new CustomFieldError("fullName", fieldError);
        }

        boolean isShortNameOccupied = this.shiftTypeService.isShortNameOccupied(shiftTypeCreationModel.getShortName());
        if (isShortNameOccupied) {
            FieldError fieldError = new FieldError("shortName", "shortName", "error.annualLeaveTypeShortName");
            throw new CustomFieldError("shortName", fieldError);
        }

        ShiftTypeViewModel shiftTypeViewModel = this.shiftTypeService.create(shiftTypeCreationModel);
        return new ResponseEntity(shiftTypeViewModel, HttpStatus.OK);
    }

    @PutMapping("/shiftTypes/{shiftTypeId}")
    public ResponseEntity update(@PathVariable long shiftTypeId,@Valid @RequestBody ShiftTypeCreationModel shiftTypeCreationModel){
        boolean isFullNameOccupied = this.shiftTypeService.isFullNameOccupied(shiftTypeCreationModel.getFullName(), shiftTypeId);
        if (isFullNameOccupied) {
            FieldError fieldError = new FieldError("fullName", "fullName", "error.annualLeaveTypeFullName");
            throw new CustomFieldError("fullName", fieldError);
        }

        boolean isShortNameOccupied = this.shiftTypeService.isShortNameOccupied(shiftTypeCreationModel.getShortName(), shiftTypeId);
        if (isShortNameOccupied) {
            FieldError fieldError = new FieldError("shortName", "shortName", "error.annualLeaveTypeShortName");
            throw new CustomFieldError("shortName", fieldError);
        }

        this.shiftTypeService.update(shiftTypeId, shiftTypeCreationModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/shiftTypes/delete/{shiftTypeId}")
    public ResponseEntity delete(@PathVariable long shiftTypeId){
        this.shiftTypeService.delete(shiftTypeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Autowired
    public void setShiftTypeService(ShiftTypeService shiftTypeService) {
        this.shiftTypeService = shiftTypeService;
    }

    @Autowired
    public void setShiftTypeGroupService(ShiftTypeGroupService shiftTypeGroupService) {
        this.shiftTypeGroupService = shiftTypeGroupService;
    }
}

