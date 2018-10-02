package com.officemaneger.areas.shiftTypeGroup.services;

import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeViewModel;
import com.officemaneger.areas.shiftType.services.ShiftTypeService;
import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;
import com.officemaneger.areas.shiftTypeGroup.models.interfaces.ShiftTypeGroupWorkScheduleSettingsView;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupScheduleViewModel;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupShortViewModel;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupWorkScheduleSettingsViewModel;
import com.officemaneger.areas.shiftTypeGroup.repositories.ShiftTypeGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShiftTypeGroupServiceImpl implements ShiftTypeGroupService {

    private ShiftTypeGroupRepository shiftTypeGroupRepository;

    private ShiftTypeService shiftTypeService;

    public ShiftTypeGroupServiceImpl() {
    }

    @Override
    public List<ShiftTypeGroupShortViewModel> getAllGroups() {
        List<ShiftTypeGroup> groups = this.shiftTypeGroupRepository.findAll();
        List<ShiftTypeGroupShortViewModel> viewModels = new ArrayList<>();
        for (ShiftTypeGroup group : groups) {
            ShiftTypeGroupShortViewModel viewModel = new ShiftTypeGroupShortViewModel();
            viewModel.setId(group.getId());
            viewModel.setGroupName(group.getGroupName());
            viewModel.setGroupOrderWeight(group.getGroupOrderWeight());

            viewModels.add(viewModel);
        }

        return viewModels;
    }

    @Override
    public List<ShiftTypeGroupWorkScheduleSettingsViewModel> getAllShiftTypeGroupWorkScheduleSettingsViewModel() {
        List<ShiftTypeGroupWorkScheduleSettingsView> groups = this.shiftTypeGroupRepository.getAllShiftTypeGroupWorkScheduleSettingsViewModels();
        List<ShiftTypeGroupWorkScheduleSettingsViewModel> viewModels = new ArrayList<>();
        for (ShiftTypeGroupWorkScheduleSettingsView group : groups) {
            ShiftTypeGroupWorkScheduleSettingsViewModel viewModel = new ShiftTypeGroupWorkScheduleSettingsViewModel();
            viewModel.setId(group.getId());
            viewModel.setGroupName(group.getGroupName());
            viewModel.setIsInSettings(false);

            viewModels.add(viewModel);
        }

        return viewModels;
    }

    @Override
    public ShiftTypeGroupScheduleViewModel getShiftTypeGroupScheduleViewModel(ShiftTypeGroup shiftTypeGroup) {
        ShiftTypeGroupScheduleViewModel viewModel = new ShiftTypeGroupScheduleViewModel();
        viewModel.setId(shiftTypeGroup.getId());
        viewModel.setGroupName(shiftTypeGroup.getGroupName());
        List<ShiftTypeViewModel> shiftTypesViewModels = new ArrayList<>();
        for (ShiftType shiftType : shiftTypeGroup.getShiftTypes()) {
            shiftTypesViewModels.add(this.shiftTypeService.getShiftTypeViewModel(shiftType));
        }
        viewModel.setGroupOrderWeight(shiftTypeGroup.getGroupOrderWeight());

        viewModel.setShiftTypes(shiftTypesViewModels);

        return viewModel;
    }

    @Autowired
    public void setShiftTypeGroupRepository(ShiftTypeGroupRepository shiftTypeGroupRepository) {
        this.shiftTypeGroupRepository = shiftTypeGroupRepository;
    }

    @Autowired
    public void setShiftTypeService(ShiftTypeService shiftTypeService) {
        this.shiftTypeService = shiftTypeService;
    }
}
