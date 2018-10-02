package com.officemaneger.areas.shiftTypeGroup.controllers;

import com.officemaneger.areas.shiftTypeGroup.services.ShiftTypeGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ShiftTypeGroupController {

    private ShiftTypeGroupService shiftTypeGroupService;

    public ShiftTypeGroupController() {
    }

    @Autowired
    public void setShiftTypeGroupService(ShiftTypeGroupService shiftTypeGroupService) {
        this.shiftTypeGroupService = shiftTypeGroupService;
    }
}
