package com.officemaneger.areas.shiftTypeGroup.services;

import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupScheduleViewModel;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupShortViewModel;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupWorkScheduleSettingsViewModel;

import java.util.List;

public interface ShiftTypeGroupService {

    List<ShiftTypeGroupShortViewModel> getAllGroups();

    List<ShiftTypeGroupWorkScheduleSettingsViewModel> getAllShiftTypeGroupWorkScheduleSettingsViewModel();

    ShiftTypeGroupScheduleViewModel getShiftTypeGroupScheduleViewModel(ShiftTypeGroup shiftTypeGroup);
}
