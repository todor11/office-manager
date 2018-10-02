package com.officemaneger.areas.workSchedule.services;

import com.officemaneger.areas.workSchedule.models.viewModels.WorkScheduleViewAllModel;

import java.time.LocalDate;
import java.util.List;

public interface WorkScheduleService {

    List<WorkScheduleViewAllModel> bossOrComputerGetWorkScheduleViewAllModels(List<LocalDate> dates, Long businessUnitId);

}
