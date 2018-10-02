package com.officemaneger.areas.annualLeaveDateInterval.services;

import com.officemaneger.areas.annualLeaveDateInterval.entities.AnnualLeaveDateInterval;
import com.officemaneger.areas.annualLeaveDateInterval.models.AnnualLeaveDateIntervalUpdateView;
import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalUpdateViewModel;
import com.officemaneger.areas.annualLeaveDateInterval.repositories.AnnualLeaveDateIntervalRepository;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.workDay.services.WorkDayService;
import com.officemaneger.utility.LocalDateParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnnualLeaveDateIntervalServiceImpl implements AnnualLeaveDateIntervalService {

    private AnnualLeaveDateIntervalRepository annualLeaveDateIntervalRepository;

    private WorkDayService workDayService;

    public AnnualLeaveDateIntervalServiceImpl() {
    }

    @Override
    public AnnualLeaveDateInterval createDateInterval(AnnualLeaveType annualLeaveType, LocalDate startDate, LocalDate endDate) {
        AnnualLeaveDateInterval annualLeaveDateInterval = new AnnualLeaveDateInterval();
        annualLeaveDateInterval.setType(annualLeaveType);
        annualLeaveDateInterval.setStartDate(startDate);
        annualLeaveDateInterval.setEndDate(endDate);

        return this.annualLeaveDateIntervalRepository.save(annualLeaveDateInterval);
    }

    @Override
    public void deleteDateInterval(AnnualLeaveDateInterval dateInterval) {
        this.annualLeaveDateIntervalRepository.delete(dateInterval);
    }

    @Override
    public List<AnnualLeaveDateIntervalUpdateViewModel> getAnnualLeaveDateIntervalUpdateViewModelsByRequest(Long requestId) {
        List<AnnualLeaveDateIntervalUpdateView> dateIntervalsFromDb = this.annualLeaveDateIntervalRepository.getAnnualLeaveDateIntervalUpdateViewModelsByRequestId(requestId);
        if (dateIntervalsFromDb == null) {
            return null;
        }

        List<AnnualLeaveDateIntervalUpdateViewModel> viewModels = new ArrayList<>();
        for (AnnualLeaveDateIntervalUpdateView annualLeaveDateIntervalUpdateView : dateIntervalsFromDb) {
            AnnualLeaveDateIntervalUpdateViewModel viewModel = new AnnualLeaveDateIntervalUpdateViewModel();
            viewModel.setId(annualLeaveDateIntervalUpdateView.getId());
            viewModel.setStartDate(annualLeaveDateIntervalUpdateView.getStartDate());
            viewModel.setEndDate(annualLeaveDateIntervalUpdateView.getEndDate());
            viewModel.setNumbOfAnnualLeave(this.workDayService.getNumbOfWorkingDaysBetweenDates(annualLeaveDateIntervalUpdateView.getStartDate(), annualLeaveDateIntervalUpdateView.getEndDate()).intValue());
            viewModel.setShiftTypeId(annualLeaveDateIntervalUpdateView.getShiftTypeId());

            viewModels.add(viewModel);
        }

        return viewModels;
    }

    @Autowired
    public void setAnnualLeaveDateIntervalRepository(AnnualLeaveDateIntervalRepository annualLeaveDateIntervalRepository) {
        this.annualLeaveDateIntervalRepository = annualLeaveDateIntervalRepository;
    }

    @Autowired
    public void setWorkDayService(WorkDayService workDayService) {
        this.workDayService = workDayService;
    }
}
