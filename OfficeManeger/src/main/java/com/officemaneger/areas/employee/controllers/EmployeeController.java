package com.officemaneger.areas.employee.controllers;

import com.officemaneger.areas.employee.models.bindingModels.CreateEmployeeModel;
import com.officemaneger.areas.employee.models.bindingModels.EmployeeUpdateModel;
import com.officemaneger.areas.employee.models.viewModels.EmployeeEditModel;
import com.officemaneger.areas.employee.models.viewModels.EmployeeFullNameViewModel;
import com.officemaneger.areas.employee.models.viewModels.EmployeeFullShortNameViewModel;
import com.officemaneger.areas.employee.models.viewModels.EmployeeViewAllModel;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.person.services.PersonService;
import com.officemaneger.areas.rank.models.viewModels.RankFullViewModel;
import com.officemaneger.areas.rank.models.viewModels.RankNameViewModel;
import com.officemaneger.areas.rank.services.RankService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.configs.errors.CustomFieldError;
import com.officemaneger.enums.PhoneType;
import com.officemaneger.utility.EGN.EGNManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeController {

    private EmployeeService employeeService;

    private UserService userService;

    private PersonService personService;

    private RankService rankService;

    public EmployeeController() {
    }

    @GetMapping("/employees/getNoPositionEmployees")
    public ResponseEntity<List<EmployeeFullNameViewModel>> getNoPositionEmployees(){
        List<EmployeeFullNameViewModel> employees = this.employeeService.getNoPositionEmployees();

        return new ResponseEntity(employees, HttpStatus.OK);
    }

    @GetMapping("/employees")
    public String getAllEmployeesPage(Model model){
        List<EmployeeViewAllModel> employees = this.employeeService.getEmployeeViewAllModels();
        model.addAttribute("employees", employees);
        model.addAttribute("title", "Всички служители");
        model.addAttribute("view", "employee/viewAllEmployees");

        return "base-layout";
    }

    @GetMapping("/employees/create")
    public String getCreateEmployeePage(Model model){
        List<String> phoneTypes = new ArrayList<>();
        for (PhoneType phoneType : PhoneType.values()) {
            phoneTypes.add(phoneType.getLongBG());
        }
        model.addAttribute("phoneTypes", phoneTypes);

        List<RankNameViewModel> ranks = this.rankService.getAllRankNameViewModels();
        model.addAttribute("ranks", ranks);

        model.addAttribute("title", "Създаване на служител");
        model.addAttribute("view", "employee/create-employee");

        return "base-layout";
    }

    @GetMapping("/employees/edit/{employeeId}")
    public String getEditEmployeePage(Model model, @PathVariable long employeeId){
        List<String> phoneTypes = new ArrayList<>();
        for (PhoneType phoneType : PhoneType.values()) {
            phoneTypes.add(phoneType.getLongBG());
        }
        model.addAttribute("phoneTypes", phoneTypes);

        List<RankNameViewModel> ranks = this.rankService.getAllRankNameViewModels();
        model.addAttribute("ranks", ranks);

        EmployeeEditModel employee = this.employeeService.getEmployeeEditModel(employeeId);
        model.addAttribute("employee", employee);

        model.addAttribute("title", "Редактиране на служител");
        model.addAttribute("view", "employee/editEmployee");

        return "base-layout";
    }

    @PostMapping("/employees/create")
    public ResponseEntity createNewEmployee(@Valid @RequestBody CreateEmployeeModel employeeCreationModel, Principal principal){
        //check is valid EGN
        boolean isValidEGN = EGNManager.isValidEGN(employeeCreationModel.getEgn());
        if (!isValidEGN) {
            FieldError fieldError = new FieldError("egn", "egn", "error.employee.invalidEGN");
            throw new CustomFieldError("egn", fieldError);
        }

        //check duplicate EGN
        boolean doWeHavePersonWithEGN = this.personService.doWeHavePersonWithEGN(employeeCreationModel.getEgn());
        if (doWeHavePersonWithEGN) {
            FieldError fieldError = new FieldError("egn", "egn", "error.employee.uniqueEGN");
            throw new CustomFieldError("egn", fieldError);
        }

        //check duplicate officiaryID
        boolean doWeHaveEmployeeWithOfficiaryID = this.employeeService.doWeHaveEmployeeWithOfficiaryID(employeeCreationModel.getOfficiaryID());
        if (doWeHaveEmployeeWithOfficiaryID) {
            FieldError fieldError = new FieldError("officiaryID", "officiaryID", "error.employee.duplicateOfficiaryID");
            throw new CustomFieldError("officiaryID", fieldError);
        }

        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                this.employeeService.create(employeeCreationModel);
                return new ResponseEntity(HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/employees/update")
    public ResponseEntity updateEmployee(@Valid @RequestBody EmployeeUpdateModel employeeUpdateModel, Principal principal){
        //check is valid EGN
        boolean isValidEGN = EGNManager.isValidEGN(employeeUpdateModel.getEgn());
        if (!isValidEGN) {
            FieldError fieldError = new FieldError("egn", "egn", "error.employee.invalidEGN");
            throw new CustomFieldError("egn", fieldError);
        }

        //check duplicate EGN
        boolean doWeHavePersonWithEGN = this.personService.doWeHavePersonWithEGNWhenUpdate(employeeUpdateModel.getId(), employeeUpdateModel.getEgn());
        if (doWeHavePersonWithEGN) {
            FieldError fieldError = new FieldError("egn", "egn", "error.employee.uniqueEGN");
            throw new CustomFieldError("egn", fieldError);
        }

        //check duplicate officiaryID
        boolean doWeHaveEmployeeWithOfficiaryID = this.employeeService.doWeHaveEmployeeWithOfficiaryIDWhenUpdate(employeeUpdateModel.getId(), employeeUpdateModel.getOfficiaryID());
        if (doWeHaveEmployeeWithOfficiaryID) {
            FieldError fieldError = new FieldError("officiaryID", "officiaryID", "error.employee.duplicateOfficiaryID");
            throw new CustomFieldError("officiaryID", fieldError);
        }

        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                this.employeeService.updateEmployee(employeeUpdateModel);
                return new ResponseEntity(HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/employees/delete/{employeeId}")
    public ResponseEntity deleteEmployee(@PathVariable long employeeId){
        this.employeeService.deleteEmployee(employeeId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/getAllActiveEmployees")
    public ResponseEntity<List<EmployeeFullShortNameViewModel>> getAllActiveEmployees(){
        List<EmployeeFullShortNameViewModel> viewModels = this.employeeService.getEmployeeFullShortNameViewModel();

        return new ResponseEntity(viewModels, HttpStatus.OK);
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPersonService(PersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setRankService(RankService rankService) {
        this.rankService = rankService;
    }
}

