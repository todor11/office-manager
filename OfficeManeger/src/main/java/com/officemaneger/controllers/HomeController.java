package com.officemaneger.controllers;

import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.microsoftOffice.word.WordParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class HomeController {

    private BusinessUnitService businessUnitService;

    @GetMapping("/")
    public String getHomePage(Model model, HttpServletRequest httpServletRequest, Authentication authentication, Principal principal){
        Object attr = httpServletRequest.getAttribute("businessUnitId");
        String businessUnitName = "";
        if (attr != null) {
            Long businessUnitId = (Long) attr;
            businessUnitName = this.businessUnitService.getBusinessUnitName(businessUnitId);
        }

        model.addAttribute("businessUnitName", businessUnitName);
        model.addAttribute("title", "Home");
        model.addAttribute("view", "home/home");

        //
        List<String> te = new ArrayList<>();
        te.add("fgd dfgd gd ");
        te.add("dato");
        WordParser.writeWordFile("src\\main\\resources\\static\\microsoftOffice\\wordTemplates\\", te);


        //

        return "base-layout";
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }
}
