package com.officemaneger.areas.role.services;

import com.officemaneger.areas.role.entities.Role;
import com.officemaneger.areas.role.models.viewModels.RoleViewModel;
import com.officemaneger.areas.role.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    private ModelMapper modelMapper;

    public RoleServiceImpl() {
    }

    @Override
    public List<RoleViewModel> getRoles() {
        List<Role> roles = this.roleRepository.findAll();
        List<RoleViewModel> viewModels = new ArrayList<>();
        for (Role role : roles) {
            viewModels.add(this.modelMapper.map(role, RoleViewModel.class));
        }

        return viewModels;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return null;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
