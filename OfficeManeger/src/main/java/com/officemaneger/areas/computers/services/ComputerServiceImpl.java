package com.officemaneger.areas.computers.services;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import com.officemaneger.areas.businessUnit.repositories.BusinessUnitRepository;
import com.officemaneger.areas.computers.entities.Computer;
import com.officemaneger.areas.computers.models.bindingModels.ComputerCreationModel;
import com.officemaneger.areas.computers.models.bindingModels.ComputerUpdateModel;
import com.officemaneger.areas.computers.models.interfaces.ComputerEditView;
import com.officemaneger.areas.computers.models.viewModels.ComputerEditViewModel;
import com.officemaneger.areas.computers.repositories.ComputerRepository;
import com.officemaneger.areas.role.entities.Role;
import com.officemaneger.areas.role.repositories.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ComputerServiceImpl implements ComputerService {

    private ComputerRepository computerRepository;

    private BusinessUnitRepository businessUnitRepository;

    private RoleRepository roleRepository;

    private ModelMapper modelMapper;

    public ComputerServiceImpl() {
    }

    @Override
    public Long getComputerIdByIP(String ip) {
        return this.computerRepository.getComputerIdByIP(ip);
    }

    @Override
    public Long addComputer(ComputerCreationModel computerCreationModel) {
        Computer computer = new Computer();
        computer.setIp(computerCreationModel.getIp());
        computer.setName(computerCreationModel.getName());
        Computer savedComputer = this.computerRepository.save(computer);

        return savedComputer.getId();
    }

    @Override
    public ComputerEditViewModel getEditComputer(Long computerId) {
        Computer computer = this.computerRepository.getOne(computerId);
        if (computer == null) {
            return null;
        }

        ComputerEditViewModel viewModel = new ComputerEditViewModel();
        viewModel.setId(computerId);
        viewModel.setIp(computer.getIp());
        viewModel.setName(computer.getName());
        Long businessUnitId = 0L;
        if (computer.getBusinessUnit() != null) {
            businessUnitId = computer.getBusinessUnit().getId();
        }
        viewModel.setBusinessUnitId(businessUnitId);

        Long roleId = 0L;
        if (computer.getRole() != null) {
            roleId = computer.getRole().getId();
        }
        viewModel.setRoleId(roleId);

        return viewModel;
    }

    @Override
    public void updateComputer(ComputerUpdateModel computerUpdateModel) {
        Computer computer = this.computerRepository.findOne(computerUpdateModel.getId());
        if (computer == null) {
            return;
        }

        if (computer.getBusinessUnit() != null) {
            BusinessUnit oldBusinessUnit = computer.getBusinessUnit();
            oldBusinessUnit.removeComputer(computer);
        }

        computer.setName(computerUpdateModel.getName());
        BusinessUnit businessUnit = this.businessUnitRepository.findOne(computerUpdateModel.getBusinessUnitId());
        computer.setBusinessUnit(businessUnit);
        if (businessUnit != null) {
            businessUnit.addComputer(computer);
        }

        if (computerUpdateModel.getRoleId() > 0) {
            Role role = this.roleRepository.getOne(computerUpdateModel.getRoleId());
            computer.setRole(role);
        }
    }

    @Override
    public Long getBusinessUnitId(String ipAddress) {
        return this.computerRepository.getBusinessUnitIdByIP(ipAddress);
    }

    @Override
    public Long getBusinessUnitIdByComputerId(Long computerId) {
        return this.computerRepository.getBusinessUnitIdByComputerId(computerId);
    }

    @Override
    public List<ComputerEditViewModel> getAllComputers() {
        List<ComputerEditView> computerEditViews = this.computerRepository.getAllComputersView();
        List<ComputerEditViewModel> viewModels = new ArrayList<>();
        for (ComputerEditView computerEditView : computerEditViews) {
            viewModels.add(this.modelMapper.map(computerEditView, ComputerEditViewModel.class));
        }

        return viewModels;
    }

    @Autowired
    public void setComputerRepository(ComputerRepository computerRepository) {
        this.computerRepository = computerRepository;
    }

    @Autowired
    public void setBusinessUnitRepository(BusinessUnitRepository businessUnitRepository) {
        this.businessUnitRepository = businessUnitRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }
}
