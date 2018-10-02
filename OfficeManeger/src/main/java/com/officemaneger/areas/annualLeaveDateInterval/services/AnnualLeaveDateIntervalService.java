package com.officemaneger.areas.annualLeaveDateInterval.services;

import com.officemaneger.areas.annualLeaveDateInterval.entities.AnnualLeaveDateInterval;
import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalUpdateViewModel;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;

import java.time.LocalDate;
import java.util.List;

public interface AnnualLeaveDateIntervalService {

    AnnualLeaveDateInterval createDateInterval(AnnualLeaveType annualLeaveType, LocalDate startDate, LocalDate endDate);

    void deleteDateInterval(AnnualLeaveDateInterval dateInterval);

    List<AnnualLeaveDateIntervalUpdateViewModel> getAnnualLeaveDateIntervalUpdateViewModelsByRequest(Long requestId);
}
