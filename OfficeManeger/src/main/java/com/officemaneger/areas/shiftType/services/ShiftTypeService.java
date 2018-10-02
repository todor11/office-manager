package com.officemaneger.areas.shiftType.services;

import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftType.models.bindingModels.ShiftTypeCreationModel;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeInWorkShiftViewModel;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeViewModel;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeWorkScheduleSettingsViewModel;

import java.util.List;

public interface ShiftTypeService {

    ShiftTypeViewModel create(ShiftTypeCreationModel shiftTypeCreationModel);

    List<ShiftTypeViewModel> getAllActiveShiftTypes();

    boolean isFullNameOccupied(String fullName);

    boolean isFullNameOccupied(String fullName, Long id);

    boolean isShortNameOccupied(String shortName);

    boolean isShortNameOccupied(String shortName, Long id);

    void update(Long shiftTypeId, ShiftTypeCreationModel shiftTypeCreationModel);

    void setNotActive(Long shiftTypeId);

    void delete(Long shiftTypeId);

    List<ShiftTypeInWorkShiftViewModel> getRegularAndActiveShiftTypes();

    List<ShiftTypeInWorkShiftViewModel> getRegularAndActiveShiftTypesByBusinessUnit(Long businessUnitId);

    List<ShiftTypeInWorkShiftViewModel> getNotRegularAndActiveShiftTypes();

    List<ShiftTypeWorkScheduleSettingsViewModel> getAllShiftTypeWorkScheduleSettingsViewModel();

    List<ShiftTypeViewModel> getShiftTypesViewModelsByIds(List<Long> shiftTypesIds);

    ShiftTypeViewModel getShiftTypeViewModel(ShiftType shiftType);
}
