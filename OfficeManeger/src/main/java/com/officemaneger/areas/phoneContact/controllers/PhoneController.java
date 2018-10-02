package com.officemaneger.areas.phoneContact.controllers;

import com.officemaneger.areas.phoneContact.services.PhoneContactService;
import com.officemaneger.enums.PhoneType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class PhoneController {

    private PhoneContactService phoneContactService;

    public PhoneController() {
    }

    @GetMapping("/phoneContactTypes")
    public ResponseEntity<List<String>> getPhoneTypes(){
        List<String> phoneTypeList = new ArrayList<>();
        PhoneType[] phoneTypes = PhoneType.values();
        for (PhoneType phoneType : phoneTypes) {
            phoneTypeList.add(phoneType.getShortBG());
        }

        return new ResponseEntity(phoneTypeList, HttpStatus.OK);
    }

    @Autowired
    public void setPhoneContactService(PhoneContactService phoneContactService) {
        this.phoneContactService = phoneContactService;
    }
}

