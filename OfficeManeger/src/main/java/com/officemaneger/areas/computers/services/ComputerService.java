package com.officemaneger.areas.computers.services;

import com.officemaneger.areas.computers.models.bindingModels.ComputerCreationModel;
import com.officemaneger.areas.computers.models.bindingModels.ComputerUpdateModel;
import com.officemaneger.areas.computers.models.viewModels.ComputerEditViewModel;

import java.util.List;

public interface ComputerService {

    Long getComputerIdByIP(String ip);

    Long addComputer(ComputerCreationModel computerCreationModel);

    ComputerEditViewModel getEditComputer(Long computerId);

    void updateComputer(ComputerUpdateModel computerUpdateModel);

    Long getBusinessUnitId(String ipAddress);

    Long getBusinessUnitIdByComputerId(Long computerId);

    List<ComputerEditViewModel> getAllComputers();
}
