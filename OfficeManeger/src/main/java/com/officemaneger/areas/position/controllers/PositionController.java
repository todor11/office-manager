package com.officemaneger.areas.position.controllers;

import com.officemaneger.areas.position.models.bindingModels.PositionAddNewModel;
import com.officemaneger.areas.position.models.viewModels.PositionNameViewModel;
import com.officemaneger.areas.position.services.PositionService;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class PositionController {

    private PositionService positionService;

    public PositionController() {
    }

    @PostMapping("/positions/addNewPosition")
    public ResponseEntity<PositionNameViewModel> addNewPosition(@Valid @RequestBody PositionAddNewModel positionAddNewModel){
        PositionNameViewModel positionNameViewModel = this.positionService.addNewPosition(positionAddNewModel);
        return new ResponseEntity(positionNameViewModel, HttpStatus.OK);
    }

    @PutMapping("/positions/updateName/{positionId}/{newPositionName}")
    public ResponseEntity updatePositionName(@PathVariable Long positionId, @PathVariable String newPositionName){
        if (newPositionName == null || positionId == null || newPositionName.isEmpty()) {
            return new ResponseEntity(HttpStatus.OK);
        }

        this.positionService.updatePositionName(newPositionName, positionId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @PutMapping("/positions/updateEmployee/{positionId}/{newEmployeeId}")
    public ResponseEntity updatePositionEmployee(@PathVariable Long positionId, @PathVariable Long newEmployeeId){
        if (positionId == null) {
            return new ResponseEntity(HttpStatus.OK);
        }

        this.positionService.updatePositionEmployee(positionId, newEmployeeId);

        return new ResponseEntity(HttpStatus.OK);
    }

    @DeleteMapping("/positions/delete/{positionId}")
    public ResponseEntity deletePosition(@PathVariable long positionId){
        this.positionService.delete(positionId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Autowired
    public void setPositionService(PositionService positionService) {
        this.positionService = positionService;
    }
}

