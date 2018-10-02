package com.officemaneger.areas.user.services;

import com.officemaneger.areas.user.models.bindingModels.*;
import com.officemaneger.areas.user.models.viewModels.AccountViewModel;
import com.officemaneger.areas.user.models.viewModels.UserNameViewModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    void register(RegistrationModel registrationModel);

    UserNameViewModel getUserNameViewModel(String username);

    boolean isUsernameOccupied(String username);

    boolean changePassword(UserPasswordChangeModel userPasswordChangeModel, String username);

    Long getAccountOwnerIdByUsername(String username);

    Long getAccountIdByUsername(String username);

    Long getAccountOwnerIdByAccountId(Long accountId);

    boolean isUsernameOccupiedToChange(String newUsername, Long accountId);

    boolean validateUserPassword(UserUpdateUsernameModel userUpdateUsernameModel);

    boolean isValidPassword(String userInputOldPassword, String username);

    boolean updateUsername(AdminUpdateUsernameModel updateUsernameModel);

    String getUserRoleAsString(String username);

    boolean bossHasAuthorityToChangeEmployee(String bossUsername, Long employeeId);

    boolean isUserBoss(String username);

    List<AccountViewModel> getEmployeeAccounts(Long employeeId);

    void resetUserPassword(Long userId);

    void changeIsActiveAccount(long userId, boolean isActive);

    boolean isUsernameRegisteredBefore(String username);

    boolean updateRole(AdminUpdateRoleModel adminUpdateRoleModel);

    void deleteAccount(Long accountId);

    boolean hasEmployeeBossRole(Long employeeId);

    void createAdmin();
}