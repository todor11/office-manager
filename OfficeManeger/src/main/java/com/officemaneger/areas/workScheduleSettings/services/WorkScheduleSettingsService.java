package com.officemaneger.areas.workScheduleSettings.services;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import com.officemaneger.areas.workScheduleSettings.entities.WorkScheduleSettings;
import com.officemaneger.areas.workScheduleSettings.models.bindingModels.WorkScheduleSettingsUpdateModel;
import com.officemaneger.areas.workScheduleSettings.models.viewModels.WorkScheduleSettingsViewModel;

import java.util.List;

public interface WorkScheduleSettingsService {

    WorkScheduleSettings creteWorkScheduleSettings(BusinessUnit businessUnit);

    WorkScheduleSettingsViewModel getScheduleSettings(Long businessUnitId);

    boolean updateSettings(WorkScheduleSettingsUpdateModel updateModel);

    Long getBusinessUnitIdBySettings(Long workScheduleSettingsId);

    List<Long> getActiveWorkShiftTypeGroupsIdsByBusinessUnitId(Long businessUnitId);

    int getNumbOfEmployeesInShiftByBusinessUnitId(Long businessUnitId);
}
