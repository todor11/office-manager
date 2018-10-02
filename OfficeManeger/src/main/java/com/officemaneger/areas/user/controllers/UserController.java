package com.officemaneger.areas.user.controllers;

import com.officemaneger.areas.employee.models.viewModels.EmployeeFullNameAndOfficiaryIdViewModel;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.role.models.viewModels.RoleViewModel;
import com.officemaneger.areas.role.services.RoleService;
import com.officemaneger.areas.user.models.bindingModels.*;
import com.officemaneger.areas.user.models.viewModels.AccountViewModel;
import com.officemaneger.configs.Errors;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;
import java.util.regex.Pattern;

@Controller
public class UserController {

    private UserService userService;

    private EmployeeService employeeService;

    private RoleService roleService;

    public UserController() {
    }

    @GetMapping("/login")
    public String getLoginPage(@RequestParam(required = false) String error, Model model){
        if(error != null){
            model.addAttribute("error", Errors.INVALID_CREDENTIALS);
        }

        model.addAttribute("title", "Login");
        model.addAttribute("view", "user/login");

        return "base-layout";
    }

    @GetMapping("/createEmployeeAccount")
    public String getCreateEmployeeAccountPage(Model model){
        List<EmployeeFullNameAndOfficiaryIdViewModel> employees = this.employeeService.getAllActiveEmployeeFullNameAndOfficiaryIdViewModel();
        model.addAttribute("employees", employees);

        List<RoleViewModel> roles = this.roleService.getRoles();
        model.addAttribute("roles", roles);

        model.addAttribute("title", "Създаване на акаунт");
        model.addAttribute("view", "user/createEmployeeAccount");

        return "base-layout";
    }

    @PostMapping("/createEmployeeAccount")
    public ResponseEntity createEmployeeAccount(@Valid @RequestBody RegistrationModel userRegistrationModel){
        boolean isUsernameOccupied = this.userService.isUsernameOccupied(userRegistrationModel.getUsername());
        if (isUsernameOccupied) {
            FieldError fieldError = new FieldError("username", "username", "error.usernameDuplicate");
            throw new CustomFieldError("username", fieldError);
        }

        if(!userRegistrationModel.getPassword().equals(userRegistrationModel.getConfirmPassword())) {
            FieldError fieldError = new FieldError("confirmPassword", "confirmPassword", "error.changePassword.confirmPassword");
            throw new CustomFieldError("confirmPassword", fieldError);
        }

        this.userService.register(userRegistrationModel);

        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/viewEmployeeAccounts/{employeeId}")
    public String viewEmployeeAccountsPage(Model model, @PathVariable long employeeId){
        List<AccountViewModel> accounts = this.userService.getEmployeeAccounts(employeeId);
        model.addAttribute("accounts", accounts);

        EmployeeFullNameAndOfficiaryIdViewModel employee = this.employeeService.getEmployeeFullNameAndOfficiaryIdViewModel(employeeId);
        model.addAttribute("employee", employee);

        List<RoleViewModel> roles = this.roleService.getRoles();
        model.addAttribute("roles", roles);

        model.addAttribute("title", "Акаунти на служителя");
        model.addAttribute("view", "user/viewEmployeeAccounts");

        return "base-layout";
    }

    @GetMapping("/changePassword")
    public String getChangePasswordPage(Model model){
        if (!model.containsAttribute("userPasswordChangeModel")) {
            model.addAttribute("userPasswordChangeModel", new UserPasswordChangeModel());
        }

        model.addAttribute("title", "Смяна на парола");
        model.addAttribute("view", "user/password-change");

        return "base-layout";
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid UserPasswordChangeModel userPasswordChangeModel, BindingResult bindingResult,
                                 Principal principal, RedirectAttributes redirectAttributes){
        if (principal == null) {
            return "redirect:/";
        }

        String username = principal.getName();

        //check old pass
        boolean isValidUserInputOldPassword = this.userService.isValidPassword(userPasswordChangeModel.getOldPassword(), username);
        if (!isValidUserInputOldPassword) {
            bindingResult.addError(new FieldError("userPasswordChangeModel", "oldPassword", "error.changePassword.oldPassword"));
        }

        //check new password
        if (!userPasswordChangeModel.getPassword().equals(userPasswordChangeModel.getConfirmPassword())) {
            bindingResult.addError(new FieldError("userPasswordChangeModel", "confirmPassword", "error.changePassword.confirmPassword"));
        }

        if(bindingResult.hasErrors()){
            redirectAttributes.addFlashAttribute("userPasswordChangeModel", userPasswordChangeModel);
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.userPasswordChangeModel", bindingResult);
            return "redirect:/changePassword";
        }

        this.userService.changePassword(userPasswordChangeModel, username);

        return "redirect:/";
    }

    @PostMapping("/resetAccountPassword/{accountId}")
    public ResponseEntity resetPassword(@PathVariable long accountId, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                this.userService.resetUserPassword(accountId);
                return new ResponseEntity(HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/company/changeIsActiveAccount/{accountId}/{isActive}")
    public ResponseEntity changeIsActiveAccount(@PathVariable long accountId, @PathVariable boolean isActive, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                this.userService.changeIsActiveAccount(accountId, isActive);
                return new ResponseEntity(HttpStatus.OK);
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/changeUsernameToAccount")
    public ResponseEntity changeUsernameToAccount(@Valid @RequestBody AdminUpdateUsernameModel adminUpdateUsernameModel, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                boolean isUsernameOccupied = this.userService.isUsernameOccupiedToChange(adminUpdateUsernameModel.getNewUsername(), adminUpdateUsernameModel.getAccountId());
                if (isUsernameOccupied) {
                    FieldError fieldError = new FieldError("username", "username", "error.usernameDuplicate");
                    throw new CustomFieldError("username", fieldError);
                }

                boolean isUpdatedUsername = this.userService.updateUsername(adminUpdateUsernameModel);
                if (isUpdatedUsername) {
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/checkUsername/{username}/{accountId}")
    public ResponseEntity checkUsername(@PathVariable String username, @PathVariable Long accountId){
        boolean isValidUsername = this.isValidUsername(username);
        if (!isValidUsername) {
            return new ResponseEntity(HttpStatus.NOT_ACCEPTABLE);//406
        }

        boolean isUsernameOccupiedToChange = this.userService.isUsernameOccupiedToChange(username, accountId);
        if (isUsernameOccupiedToChange) {
            return new ResponseEntity(HttpStatus.CONFLICT);//409
        } else {
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @PutMapping("/changeRoleToAccount")
    public ResponseEntity changeRoleToAccount(@Valid @RequestBody AdminUpdateRoleModel adminUpdateRoleModel, Principal principal){
        String username = principal.getName();
        String roleAsString = this.userService.getUserRoleAsString(username);
        switch (roleAsString) {
            case "ROLE_ADMIN":
                boolean isUpdatedRole = this.userService.updateRole(adminUpdateRoleModel);
                if (isUpdatedRole) {
                    return new ResponseEntity(HttpStatus.OK);
                } else {
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
                }
            default:
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/deleteAccount/{accountId}")
    public ResponseEntity deleteAccount(@PathVariable Long accountId){
        this.userService.deleteAccount(accountId);
        return new ResponseEntity(HttpStatus.OK);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    private boolean isValidUsername(String username) {
        String pattern = "^[a-zA-Z0-9_]{5,30}$";
        return Pattern.matches(pattern, username);
    }

    private void setAdminIfNotExist() {
        this.userService.createAdmin();
    }
}