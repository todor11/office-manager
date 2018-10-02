package com.officemaneger.areas.annualLeaveType.controllers;

import com.officemaneger.areas.annualLeaveType.models.bindingModels.AnnualLeaveTypeCreationModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeFullNameViewModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeViewModel;
import com.officemaneger.areas.annualLeaveType.services.AnnualLeaveTypeService;
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
public class AnnualLeaveTypeController {

    private AnnualLeaveTypeService annualLeaveTypeService;

    public AnnualLeaveTypeController() {
    }

    @GetMapping("/annualLeaveTypes")
    public String getAnnualLeaveTypesPage(Model model){
        List<AnnualLeaveTypeViewModel> annualLeaveTypes = this.annualLeaveTypeService.getAllActiveAnnualLeaveTypes();
        model.addAttribute("annualLeaveTypes", annualLeaveTypes);
        model.addAttribute("title", "Видове отпуски");
        model.addAttribute("view", "annualLeaveType/annualLeaveTypes");

        return "base-layout";
    }

    @PostMapping("/annualLeaveTypes/add")
    public ResponseEntity<AnnualLeaveTypeViewModel> saveAnnualLeaveType(@Valid @RequestBody AnnualLeaveTypeCreationModel annualLeaveTypeCreationModel){
        boolean isFullNameOccupied = this.annualLeaveTypeService.isFullNameOccupied(annualLeaveTypeCreationModel.getFullName());
        if (isFullNameOccupied) {
            FieldError fieldError = new FieldError("fullName", "fullName", "error.annualLeaveTypeFullName");
            throw new CustomFieldError("fullName", fieldError);
        }

        boolean isShortNameOccupied = this.annualLeaveTypeService.isShortNameOccupied(annualLeaveTypeCreationModel.getShortName());
        if (isShortNameOccupied) {
            FieldError fieldError = new FieldError("shortName", "shortName", "error.annualLeaveTypeShortName");
            throw new CustomFieldError("shortName", fieldError);
        }

        AnnualLeaveTypeViewModel annualLeaveTypeViewModel = this.annualLeaveTypeService.create(annualLeaveTypeCreationModel);
        return new ResponseEntity(annualLeaveTypeViewModel, HttpStatus.OK);
    }

    @PutMapping("/annualLeaveTypes/{annualLeaveTypeId}")
    public ResponseEntity update(@PathVariable long annualLeaveTypeId,@Valid @RequestBody AnnualLeaveTypeCreationModel annualLeaveTypeCreationModel){
        boolean isFullNameOccupied = this.annualLeaveTypeService.isFullNameOccupied(annualLeaveTypeCreationModel.getFullName(), annualLeaveTypeId);
        if (isFullNameOccupied) {
            FieldError fieldError = new FieldError("fullName", "fullName", "error.annualLeaveTypeFullName");
            throw new CustomFieldError("fullName", fieldError);
        }

        boolean isShortNameOccupied = this.annualLeaveTypeService.isShortNameOccupied(annualLeaveTypeCreationModel.getShortName(), annualLeaveTypeId);
        if (isShortNameOccupied) {
            FieldError fieldError = new FieldError("shortName", "shortName", "error.annualLeaveTypeShortName");
            throw new CustomFieldError("shortName", fieldError);
        }

        this.annualLeaveTypeService.update(annualLeaveTypeId, annualLeaveTypeCreationModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/annualLeaveTypes/delete/{annualLeaveTypeId}")
    public ResponseEntity delete(@PathVariable long annualLeaveTypeId){
        this.annualLeaveTypeService.delete(annualLeaveTypeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getAllAnnualLeaveTypes")
    public ResponseEntity<List<AnnualLeaveTypeFullNameViewModel>> getAllAnnualLeaveTypes(){
        List<AnnualLeaveTypeFullNameViewModel> viewModels = this.annualLeaveTypeService.getAllActiveAnnualLeaveTypesFullNames();

        return new ResponseEntity(viewModels, HttpStatus.OK);
    }

    @Autowired
    public void setAnnualLeaveTypeService(AnnualLeaveTypeService annualLeaveTypeService) {
        this.annualLeaveTypeService = annualLeaveTypeService;
    }
}

