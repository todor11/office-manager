package com.officemaneger.areas.user.services;

import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.repositories.EmployeeRepository;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.role.entities.Role;
import com.officemaneger.areas.role.models.viewModels.RoleViewModel;
import com.officemaneger.areas.role.repositories.RoleRepository;
import com.officemaneger.areas.role.services.RoleService;
import com.officemaneger.areas.user.models.bindingModels.*;
import com.officemaneger.areas.user.models.interfaces.AccountView;
import com.officemaneger.areas.user.models.viewModels.AccountViewModel;
import com.officemaneger.areas.user.models.viewModels.UserNameViewModel;
import com.officemaneger.configs.Errors;
import com.officemaneger.areas.user.entities.User;
import com.officemaneger.areas.user.repositories.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserServiceImpl implements UserService {

    private static final String DEFAULT_USER_PASSWORD = "123123";

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    private UserRepository userRepository;

    private EmployeeRepository employeeRepository;

    private RoleRepository roleRepository;

    private ModelMapper modelMapper;

    private EmployeeService employeeService;

    private BusinessUnitService businessUnitService;

    private RoleService roleService;

    public UserServiceImpl() {
    }

    @Override
    @Transactional
    public void register(RegistrationModel registrationModel) {
        User user = new User();
        user.setUsername(registrationModel.getUsername());

        String encryptedPassword = this.bCryptPasswordEncoder.encode(registrationModel.getPassword());
        user.setPassword(encryptedPassword);
        user.setAccountNonExpired(true);
        user.setAccountNonLocked(true);
        user.setEnabled(true);
        user.setCredentialsNonExpired(true);
        if (registrationModel.getEmployeeId() != null) {
            Employee employee = this.employeeRepository.findOne(registrationModel.getEmployeeId());
            user.setEmployee(employee);
        }

        Role role = this.roleRepository.findOne(registrationModel.getRoleId());
        Set<Role> authorities = new HashSet<>();
        authorities.add(role);
        user.setAuthorities(authorities);
        user.setId(null);
        this.userRepository.save(user);
    }

    @Override
    public UserNameViewModel getUserNameViewModel(String username) {
        UserNameViewModel viewModel = new UserNameViewModel();
        String usernameFromDB = this.userRepository.getUsername(username);
        if (usernameFromDB == null) {
            return null;
        }

        viewModel.setUsername(usernameFromDB);

        return viewModel;
    }

    @Override
    @Transactional
    public boolean changePassword(UserPasswordChangeModel userPasswordChangeModel, String username) {
        User user = this.userRepository.findOneByUsername(username);
        if (user == null || user.getPassword() == null) {
            return false;
        }

        if (!this.bCryptPasswordEncoder.matches(userPasswordChangeModel.getOldPassword(), user.getPassword())) {
            return false;
        }

        String newPassword = this.bCryptPasswordEncoder.encode(userPasswordChangeModel.getPassword());
        user.setPassword(newPassword);

        return true;
    }

    @Override
    public boolean isValidPassword(String userInputOldPassword, String username) {
        User user = this.userRepository.findOneByUsername(username);
        if (user == null || user.getPassword() == null) {
            return false;
        }

        if (!this.bCryptPasswordEncoder.matches(userInputOldPassword, user.getPassword())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean isUsernameOccupied(String username) {
        String usernameFromDB = this.userRepository.getUsername(username);
        if (usernameFromDB == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean bossHasAuthorityToChangeEmployee(String bossUsername, Long employeeId) {
        if (!this.isUserBoss(bossUsername)) {
            return false;
        }

        Long employeeBusinessUnitId = this.employeeService.getBusinessUnitIdByEmployeeId(employeeId);
        Long bossId = this.userRepository.getAccountOwnerIdByUsername(bossUsername);
        Long bossBusinessUnitId = this.employeeService.getBusinessUnitIdByEmployeeId(bossId);
        if (employeeBusinessUnitId.equals(bossBusinessUnitId)) {
            return true;
        }

        Long mainUnitId = employeeBusinessUnitId;
        while (mainUnitId != null) {
            if (bossBusinessUnitId.equals(mainUnitId)) {
                return true;
            }

            mainUnitId = this.businessUnitService.getMainUnitIdByUnitId(mainUnitId);
        }

        return false;
    }

    @Override
    public boolean isUserBoss(String username) {
        String roleAsString = "ROLE_BOSS";
        return this.dasUserHaveRole(username, roleAsString);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findOneByUsername(username);
        if(user == null){
            throw new UsernameNotFoundException(Errors.INVALID_CREDENTIALS);
        }

        return user;
    }

    @Override
    public String getUserRoleAsString(String username) {
        List<Role> authorities = this.userRepository.getUserAuthorities(username);
        if (authorities == null || authorities.isEmpty()) {
            return null;
        }

        return authorities.get(0).getAuthority();

    }

    @Override
    public Long getAccountOwnerIdByUsername(String username) {
        return this.userRepository.getAccountOwnerIdByUsername(username);
    }

    @Override
    public Long getAccountIdByUsername(String username) {
        return this.userRepository.getAccountIdByUsername(username);
    }

    @Override
    public Long getAccountOwnerIdByAccountId(Long accountId) {
        return this.userRepository.getAccountOwnerIdByAccountId(accountId);
    }

    @Override
    public boolean isUsernameOccupiedToChange(String newUsername, Long accountId) {
        if (!this.userRepository.exists(accountId)) {
            return true;
        }

        Long existingUserIdByUsername = this.userRepository.getAccountIdByUsername(newUsername);
        if (existingUserIdByUsername == null || accountId.equals(existingUserIdByUsername)) {
            return false;
        }

        return true;
    }

    @Override
    public boolean validateUserPassword(UserUpdateUsernameModel userUpdateUsernameModel) {
        User user = this.userRepository.findOne(userUpdateUsernameModel.getAccountId());
        if (user == null || !this.bCryptPasswordEncoder.matches(userUpdateUsernameModel.getPassword(), user.getPassword())) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public boolean updateUsername(AdminUpdateUsernameModel updateUsernameModel) {
        if (updateUsernameModel.getNewUsername() == null || updateUsernameModel.getNewUsername().isEmpty()) {
            return false;
        }

        User user = this.userRepository.findOne(updateUsernameModel.getAccountId());
        user.setUsername(updateUsernameModel.getNewUsername());

        return true;
    }

    @Override
    @Transactional
    public boolean updateRole(AdminUpdateRoleModel adminUpdateRoleModel) {
        if (adminUpdateRoleModel.getAccountId() == null || adminUpdateRoleModel.getAccountId() < 1L ||
                adminUpdateRoleModel.getRoleId() == null || adminUpdateRoleModel.getRoleId() < 1L) {
            return false;
        }

        User user = this.userRepository.findOne(adminUpdateRoleModel.getAccountId());
        if (user == null) {
            return false;
        }

        Role role = this.roleRepository.findOne(adminUpdateRoleModel.getRoleId());
        if (role == null) {
            return false;
        }
        Set<Role> newAuthority = new HashSet<>();
        newAuthority.add(role);

        user.setAuthorities(newAuthority);
        return true;
    }

    @Override
    @Transactional
    public void resetUserPassword(Long userId) {
        User user = this.userRepository.findOne(userId);
        if (user == null) {
            return;
        }

        String cryptPassword = this.bCryptPasswordEncoder.encode(DEFAULT_USER_PASSWORD);
        user.setPassword(cryptPassword);
    }

    @Override
    @Transactional
    public void changeIsActiveAccount(long userId, boolean isActive) {
        User user = this.userRepository.findOne(userId);
        if (user == null) {
            return;
        }

        user.setEnabled(isActive);
    }

    @Override
    @Transactional
    public void deleteAccount(Long accountId) {
        this.userRepository.delete(accountId);
    }

    @Override
    public boolean hasEmployeeBossRole(Long employeeId) {
        List<String> currentUserUsernames = this.userRepository.getAccountsUsernamesByEmployeeId(employeeId);
        for (String currentUserUsername : currentUserUsernames) {
            List<Role> roles = this.userRepository.getUserAuthorities(currentUserUsername);
            Role role = roles.get(0);
            if (role.getAuthority().equals("ROLE_BOSS")) {
                return true;
            }
        }

        return false;
    }

    @Override
    public List<AccountViewModel> getEmployeeAccounts(Long employeeId) {
        List<AccountView> accountViews = this.userRepository.getAccountsViewByEmployeeId(employeeId);
        List<AccountViewModel> viewModels = new ArrayList<>();
        for (AccountView accountView : accountViews) {
            AccountViewModel viewModel = new AccountViewModel();
            viewModel.setId(accountView.getId());
            viewModel.setIsEnabled(accountView.getIsEnabled());
            viewModel.setUsername(accountView.getUsername());
            List<Role> roles = this.userRepository.getUserAuthorities(accountView.getUsername());
            Role role = roles.get(0);
            RoleViewModel roleViewModel = new RoleViewModel();
            roleViewModel.setId(role.getId());
            roleViewModel.setAuthority(role.getAuthority());

            viewModel.setRole(roleViewModel);
            viewModels.add(viewModel);
        }


        return viewModels;
    }

    @Override
    public boolean isUsernameRegisteredBefore(String username) {
        String resultUsername = this.userRepository.getUsername(username);
        if (resultUsername == null) {
            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public void createAdmin() {
        RegistrationModel userRegistrationModel = new RegistrationModel();
        userRegistrationModel.setUsername("todor13");
        userRegistrationModel.setPassword("datoAdmin");
        userRegistrationModel.setConfirmPassword("datoAdmin");
        userRegistrationModel.setRoleId(1L);
        this.register(userRegistrationModel);
    }

    @Autowired
    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    @Autowired
    public void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    private boolean dasUserHaveRole(String username, String roleAsString) {
        String userRoleAsString = this.getUserRoleAsString(username);
        if (userRoleAsString.equals(roleAsString)) {
            return true;
        }

        return false;
    }
}
