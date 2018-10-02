package com.officemaneger.areas.workShift.services;

import com.officemaneger.areas.workShift.models.viewModels.WorkShiftViewAllModel;

import java.time.LocalDate;
import java.util.List;

public interface WorkShiftService {

    List<WorkShiftViewAllModel> getWorkShiftViewAllModels(List<LocalDate> dates);

    List<WorkShiftViewAllModel> getWorkShiftViewAllModelsByComputerId(List<LocalDate> dates, Long computerId);

    List<WorkShiftViewAllModel> getWorkShiftViewAllModelsByBossId(List<LocalDate> dates, Long bossId);
}
