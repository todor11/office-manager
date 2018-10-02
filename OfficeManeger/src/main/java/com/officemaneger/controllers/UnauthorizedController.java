package com.officemaneger.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UnauthorizedController {

    public UnauthorizedController() {
    }

    @GetMapping("/unauthorized")
    public String getUnauthorizedPage(Model model){
        model.addAttribute("title", "home");
        model.addAttribute("view", "unauthorized");

        return "base-layout";
    }
}
