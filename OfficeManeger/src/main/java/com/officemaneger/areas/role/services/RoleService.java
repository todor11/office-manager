package com.officemaneger.areas.role.services;

import com.officemaneger.areas.role.models.viewModels.RoleViewModel;
import com.officemaneger.areas.user.models.bindingModels.RegistrationModel;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface RoleService extends UserDetailsService {

    List<RoleViewModel> getRoles();

}