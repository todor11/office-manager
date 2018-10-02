package com.officemaneger.areas.computers.controllers;

import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitNameViewModel;
import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.computers.models.bindingModels.ComputerCreationModel;
import com.officemaneger.areas.computers.models.bindingModels.ComputerUpdateModel;
import com.officemaneger.areas.computers.models.viewModels.ComputerEditViewModel;
import com.officemaneger.areas.computers.services.ComputerService;
import com.officemaneger.areas.role.models.viewModels.RoleViewModel;
import com.officemaneger.areas.role.services.RoleService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@Controller
public class ComputerController {

    private ComputerService computerService;

    private UserService userService;

    private RoleService roleService;

    private BusinessUnitService businessUnitService;

    public ComputerController() {
    }

    @GetMapping("/addComputer")
    public String addComputerPage(Model model) {
        model.addAttribute("title", "Добавяне на компютър");
        model.addAttribute("view", "computers/addComputer");

        return "base-layout";
    }

    @PostMapping("/addComputer")
    public ResponseEntity<Long> addComputer(@Valid @RequestBody ComputerCreationModel computerCreationModel, Principal principal, HttpServletRequest httpServletRequest){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        String ipAddress = (String) httpServletRequest.getAttribute("ip");
        computerCreationModel.setIp(ipAddress);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                Long savedComputerId = this.computerService.addComputer(computerCreationModel);
                return new ResponseEntity(savedComputerId, HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/computer/edit/{computerId}")
    public String editComputerPage(Model model) {
        List<BusinessUnitNameViewModel> bussinessUnits = this.businessUnitService.getAllBusinessUnitNameViewModels();
        List<RoleViewModel> roles = this.roleService.getRoles();

        model.addAttribute("title", "Редактиране на компютър");
        model.addAttribute("view", "computers/editComputer");
        model.addAttribute("bussinessUnits", bussinessUnits);
        model.addAttribute("roles", roles);

        return "base-layout";
    }

    @GetMapping("/getEditComputer/{computerId}")
    public ResponseEntity<ComputerEditViewModel> getEditComputer(@PathVariable Long computerId, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                ComputerEditViewModel viewModel = this.computerService.getEditComputer(computerId);
                return new ResponseEntity(viewModel, HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/computer/update")
    public ResponseEntity updateComputer(@Valid @RequestBody ComputerUpdateModel computerUpdateModel, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                this.computerService.updateComputer(computerUpdateModel);
                return new ResponseEntity(HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @Autowired
    public void setComputerService(ComputerService computerService) {
        this.computerService = computerService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }
}
