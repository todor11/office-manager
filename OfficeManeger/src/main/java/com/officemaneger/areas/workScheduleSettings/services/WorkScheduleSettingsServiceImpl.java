package com.officemaneger.areas.workScheduleSettings.services;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeWorkScheduleSettingsViewModel;
import com.officemaneger.areas.shiftType.repositories.ShiftTypeRepository;
import com.officemaneger.areas.shiftType.services.ShiftTypeService;
import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupWorkScheduleSettingsViewModel;
import com.officemaneger.areas.shiftTypeGroup.repositories.ShiftTypeGroupRepository;
import com.officemaneger.areas.shiftTypeGroup.services.ShiftTypeGroupService;
import com.officemaneger.areas.workScheduleSettings.entities.WorkScheduleSettings;
import com.officemaneger.areas.workScheduleSettings.models.bindingModels.WorkScheduleSettingsUpdateModel;
import com.officemaneger.areas.workScheduleSettings.models.interfaces.SettingsView;
import com.officemaneger.areas.workScheduleSettings.models.viewModels.WorkScheduleSettingsViewModel;
import com.officemaneger.areas.workScheduleSettings.repositories.WorkScheduleSettingsRepository;
import com.officemaneger.configs.errors.CustomFieldError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class WorkScheduleSettingsServiceImpl implements WorkScheduleSettingsService {

    private static int DEFAULT_NUMB_OF_EMPLOYEES = 2;

    private WorkScheduleSettingsRepository workScheduleSettingsRepository;

    private ShiftTypeRepository shiftTypeRepository;

    private ShiftTypeGroupRepository shiftTypeGroupRepository;

    private ShiftTypeGroupService shiftTypeGroupService;

    private ShiftTypeService shiftTypeService;

    public WorkScheduleSettingsServiceImpl() {
    }

    @Override
    public WorkScheduleSettings creteWorkScheduleSettings(BusinessUnit businessUnit) {
        WorkScheduleSettings settings = new WorkScheduleSettings();
        settings.setBusinessUnit(businessUnit);
        settings.setNumbOfEmployeesInShift(DEFAULT_NUMB_OF_EMPLOYEES);

        return this.workScheduleSettingsRepository.save(settings);
    }

    @Override
    public WorkScheduleSettingsViewModel getScheduleSettings(Long businessUnitId) {
        Long settingsId = this.workScheduleSettingsRepository.getSettingsIdByBusinessUnitId(businessUnitId);
        if (settingsId == null) {
            return null;
        }

        WorkScheduleSettingsViewModel viewModel = new WorkScheduleSettingsViewModel();
        viewModel.setId(settingsId);
        SettingsView settingsView = this.workScheduleSettingsRepository.getSettingsView(settingsId);
        viewModel.setNumbOfEmployeesInShift(settingsView.getNumbOfEmployeesInShift());
        viewModel.setIsOnDoubleShiftRegime(settingsView.getIsOnDoubleShiftRegime());
        viewModel.setIsOnTripleShiftRegime(settingsView.getIsOnTripleShiftRegime());
        List<Long> shiftTypeGroupsToObserveIds = this.workScheduleSettingsRepository.getShiftTypeGroupsToObserveIds(settingsId);
        List<ShiftTypeGroupWorkScheduleSettingsViewModel> allShiftTypeGroups = this.shiftTypeGroupService.getAllShiftTypeGroupWorkScheduleSettingsViewModel();
        for (ShiftTypeGroupWorkScheduleSettingsViewModel shiftTypeGroup : allShiftTypeGroups) {
            if (shiftTypeGroupsToObserveIds.contains(shiftTypeGroup.getId())) {
                shiftTypeGroup.setIsInSettings(true);
            }
        }
        viewModel.setAllShiftTypeGroups(allShiftTypeGroups);


        return viewModel;
    }

    @Override
    public boolean updateSettings(WorkScheduleSettingsUpdateModel updateModel) {
        WorkScheduleSettings settings = this.workScheduleSettingsRepository.findOne(updateModel.getId());
        if (settings == null) {
            FieldError fieldError = new FieldError("scheduleSettingsUpdateModel", "scheduleSettingsUpdateModel", "error.workScheduleSettings.id");
            throw new CustomFieldError("scheduleSettingsUpdateModel", fieldError);
        }

        List<ShiftTypeGroup> newShiftTypeGroups = new ArrayList<>();
        for (Long shiftTypeGroupId : updateModel.getShiftTypeGroupsToObserveIds()) {
            ShiftTypeGroup shiftTypeGroup = this.shiftTypeGroupRepository.findOne(shiftTypeGroupId);
            if (shiftTypeGroup == null) {
                FieldError fieldError = new FieldError("scheduleSettingsUpdateModel", "scheduleSettingsUpdateModel", "error.workScheduleSettings.shiftTypeId");
                throw new CustomFieldError("scheduleSettingsUpdateModel", fieldError);
            }

            newShiftTypeGroups.add(shiftTypeGroup);
        }

        settings.setShiftTypeGroupsToObserve(newShiftTypeGroups);
        settings.setNumbOfEmployeesInShift(updateModel.getNumbOfEmployeesInShift());
        settings.setIsOnDoubleShiftRegime(updateModel.getIsOnDoubleShiftRegime());
        settings.setIsOnTripleShiftRegime(updateModel.getIsOnTripleShiftRegime());

        return true;
    }

    @Override
    public List<Long> getActiveWorkShiftTypeGroupsIdsByBusinessUnitId(Long businessUnitId) {
        return this.workScheduleSettingsRepository.getActiveWorkShiftTypeGroupsIdsByBusinessUnitId(businessUnitId);
    }

    @Override
    public int getNumbOfEmployeesInShiftByBusinessUnitId(Long businessUnitId) {
        return this.workScheduleSettingsRepository.getNumbOfEmployeesInShiftByBusinessUnitId(businessUnitId);
    }

    @Override
    public Long getBusinessUnitIdBySettings(Long workScheduleSettingsId) {
        return this.workScheduleSettingsRepository.getBusinessUnitIdBySettingsId(workScheduleSettingsId);
    }

    @Autowired
    public void setShiftTypeRepository(ShiftTypeRepository shiftTypeRepository) {
        this.shiftTypeRepository = shiftTypeRepository;
    }

    @Autowired
    public void setWorkScheduleSettingsRepository(WorkScheduleSettingsRepository workScheduleSettingsRepository) {
        this.workScheduleSettingsRepository = workScheduleSettingsRepository;
    }

    @Autowired
    public void setShiftTypeService(ShiftTypeService shiftTypeService) {
        this.shiftTypeService = shiftTypeService;
    }

    @Autowired
    public void setShiftTypeGroupService(ShiftTypeGroupService shiftTypeGroupService) {
        this.shiftTypeGroupService = shiftTypeGroupService;
    }

    @Autowired
    public void setShiftTypeGroupRepository(ShiftTypeGroupRepository shiftTypeGroupRepository) {
        this.shiftTypeGroupRepository = shiftTypeGroupRepository;
    }
}
