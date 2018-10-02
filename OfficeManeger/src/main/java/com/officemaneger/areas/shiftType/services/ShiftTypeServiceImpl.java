package com.officemaneger.areas.shiftType.services;

import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.annualLeaveType.repositories.AnnualLeaveTypeRepository;
import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftType.models.bindingModels.ShiftTypeCreationModel;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeInWorkShiftViewModel;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeViewModel;
import com.officemaneger.areas.shiftType.models.viewModels.ShiftTypeWorkScheduleSettingsViewModel;
import com.officemaneger.areas.shiftType.repositories.ShiftTypeRepository;
import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;
import com.officemaneger.areas.shiftTypeGroup.repositories.ShiftTypeGroupRepository;
import com.officemaneger.areas.workScheduleSettings.models.viewModels.WorkScheduleSettingsViewModel;
import com.officemaneger.areas.workScheduleSettings.services.WorkScheduleSettingsService;
import com.officemaneger.configs.errors.CustomFieldError;
import com.officemaneger.utility.Constants;
import com.officemaneger.utility.TimeCalculator;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.FieldError;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class ShiftTypeServiceImpl implements ShiftTypeService {

    private ShiftTypeRepository shiftTypeRepository;

    private AnnualLeaveTypeRepository annualLeaveTypeRepository;

    private ShiftTypeGroupRepository shiftTypeGroupRepository;

    private ModelMapper modelMapper;

    private WorkScheduleSettingsService workScheduleSettingsService;

    public ShiftTypeServiceImpl() {
    }

    @Override
    public ShiftTypeViewModel create(ShiftTypeCreationModel shiftTypeCreationModel) {
        ShiftType shiftType = new ShiftType();
        shiftType.setFullName(shiftTypeCreationModel.getFullName());
        shiftType.setShortName(shiftTypeCreationModel.getShortName());
        shiftType.setShiftDuration(shiftTypeCreationModel.getShiftDuration());
        shiftType.setBreakDuration(shiftTypeCreationModel.getBreakDuration());
        shiftType.setStartTime(shiftTypeCreationModel.getStartTime());
        shiftType.setEndTime(shiftTypeCreationModel.getEndTime());
        shiftType.setSecondStartTime(shiftTypeCreationModel.getSecondStartTime());
        shiftType.setSecondEndTime(shiftTypeCreationModel.getSecondEndTime());
        shiftType.setIsRegularShift(shiftTypeCreationModel.getIsRegularShift());
        shiftType.setIsEndOnNextDay(shiftTypeCreationModel.getIsEndOnNextDay());

        shiftType.setIsActive(true);
        boolean isEndOnNextDay = false;
        if (shiftTypeCreationModel.getStartTime() != null && shiftTypeCreationModel.getEndTime() != null) {
            if (shiftTypeCreationModel.getSecondStartTime() != null && shiftTypeCreationModel.getSecondEndTime() != null) {
                isEndOnNextDay = shiftTypeCreationModel.getEndTime().isAfter(shiftTypeCreationModel.getSecondEndTime());
            } else {
                isEndOnNextDay = shiftTypeCreationModel.getStartTime().isAfter(shiftTypeCreationModel.getEndTime());
            }
        }


        shiftType.setIsEndOnNextDay(isEndOnNextDay);

        if (shiftTypeCreationModel.getIsRegularShift() && shiftTypeCreationModel.getShiftDuration() == null) {
            shiftType.setShiftDuration(Constants.DEFAULT_SHIFT_DURATION);
        }

        if (shiftTypeCreationModel.getIsRegularShift() && shiftTypeCreationModel.getBreakDuration() == null) {
            shiftType.setBreakDuration(Constants.DEFAULT_BREAK_DURATION);
        }

        shiftType.setCanBeOnDoubleShiftRegime(shiftTypeCreationModel.getCanBeOnDoubleShiftRegime());
        shiftType.setCanBeOnTripleShiftRegime(shiftTypeCreationModel.getCanBeOnTripleShiftRegime());
        shiftType.setCanBeOnWorkDay(shiftTypeCreationModel.getCanBeOnWorkDay());
        shiftType.setCanBeOnRestDay(shiftTypeCreationModel.getCanBeOnRestDay());
        shiftType.setCanBeOnLastWorkDayBeforeRestDay(shiftTypeCreationModel.getCanBeOnLastWorkDayBeforeRestDay());
        shiftType.setCanBeOnLastRestDayBeforeWorkDay(shiftTypeCreationModel.getCanBeOnLastRestDayBeforeWorkDay());

        ShiftTypeGroup shiftTypeGroup = null;
        if (shiftTypeCreationModel.getShiftTypeGroupId() > 0) {
            shiftTypeGroup = this.shiftTypeGroupRepository.findOne(shiftTypeCreationModel.getShiftTypeGroupId());
            if (shiftTypeGroup != null) {
                if (!shiftTypeGroup.addShiftType(shiftType)) {
                    FieldError fieldError = new FieldError("shiftTypeGroup", "shiftTypeGroup", "error.shiftTypeGroup.shiftTypeCollision");
                    throw new CustomFieldError("shiftTypeGroup", fieldError);
                }
            }
        }
        shiftType.setShiftTypeGroup(shiftTypeGroup);

        ShiftType savedShiftType = this.shiftTypeRepository.save(shiftType);
        return this.convertToShiftTypeViewModel(savedShiftType);
    }

    @Override
    public List<ShiftTypeViewModel> getAllActiveShiftTypes() {
        List<ShiftType> activeShiftTypes = this.shiftTypeRepository.findAllByIsActiveOrderByIsRegularShiftDescShortNameAsc(true);
        List<ShiftTypeViewModel> resultList = new ArrayList<>();
        for (ShiftType activeShiftType : activeShiftTypes) {
            resultList.add(this.convertToShiftTypeViewModel(activeShiftType));
        }

        return resultList;
    }

    @Override
    public List<ShiftTypeViewModel> getShiftTypesViewModelsByIds(List<Long> shiftTypesIds) {
        List<ShiftType> shiftTypes = this.shiftTypeRepository.findAllByIdOrderByIsRegularShiftDescShortNameAsc(shiftTypesIds);
        List<ShiftTypeViewModel> resultList = new ArrayList<>();
        for (ShiftType shiftType : shiftTypes) {
            resultList.add(this.convertToShiftTypeViewModel(shiftType));
        }

        return resultList;
    }

    @Override
    public boolean isFullNameOccupied(String fullName) {
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOneByFullName(fullName);
        ShiftType shiftType = this.shiftTypeRepository.findOneByFullName(fullName);
        return !(annualLeaveType == null && shiftType == null);
    }

    @Override
    public boolean isFullNameOccupied(String shiftTypeFullName, Long id) {
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOneByFullName(shiftTypeFullName);
        ShiftType shiftType = this.shiftTypeRepository.findOneByFullName(shiftTypeFullName);

        return this.isOccupied(annualLeaveType, shiftType, id);
    }

    @Override
    public boolean isShortNameOccupied(String shortName) {
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOneByShortName(shortName);
        ShiftType shiftType = this.shiftTypeRepository.findOneByShortName(shortName);
        return !(annualLeaveType == null && shiftType == null);
    }

    @Override
    public boolean isShortNameOccupied(String shortName, Long id) {
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOneByShortName(shortName);
        ShiftType shiftType = this.shiftTypeRepository.findOneByShortName(shortName);

        return this.isOccupied(annualLeaveType, shiftType, id);
    }

    @Override
    public void update(Long shiftTypeId, ShiftTypeCreationModel shiftTypeCreationModel) {
        ShiftType shiftType = this.shiftTypeRepository.findOne(shiftTypeId);
        shiftType.setFullName(shiftTypeCreationModel.getFullName());
        shiftType.setShortName(shiftTypeCreationModel.getShortName());
        shiftType.setShiftDuration(shiftTypeCreationModel.getShiftDuration());
        shiftType.setBreakDuration(shiftTypeCreationModel.getBreakDuration());
        shiftType.setStartTime(shiftTypeCreationModel.getStartTime());
        shiftType.setEndTime(shiftTypeCreationModel.getEndTime());
        shiftType.setSecondStartTime(shiftTypeCreationModel.getSecondStartTime());
        shiftType.setSecondEndTime(shiftTypeCreationModel.getSecondEndTime());
        shiftType.setIsRegularShift(shiftTypeCreationModel.getIsRegularShift());
        shiftType.setIsEndOnNextDay(shiftTypeCreationModel.getIsEndOnNextDay());
        shiftType.setIsActive(shiftTypeCreationModel.getIsActive());

        shiftType.setCanBeOnDoubleShiftRegime(shiftTypeCreationModel.getCanBeOnDoubleShiftRegime());
        shiftType.setCanBeOnTripleShiftRegime(shiftTypeCreationModel.getCanBeOnTripleShiftRegime());
        shiftType.setCanBeOnWorkDay(shiftTypeCreationModel.getCanBeOnWorkDay());
        shiftType.setCanBeOnRestDay(shiftTypeCreationModel.getCanBeOnRestDay());
        shiftType.setCanBeOnLastWorkDayBeforeRestDay(shiftTypeCreationModel.getCanBeOnLastWorkDayBeforeRestDay());
        shiftType.setCanBeOnLastRestDayBeforeWorkDay(shiftTypeCreationModel.getCanBeOnLastRestDayBeforeWorkDay());

        ShiftTypeGroup oldShiftTypeGroup = shiftType.getShiftTypeGroup();
        if (oldShiftTypeGroup != null) {
            oldShiftTypeGroup.removeShiftType(shiftType);
        }

        ShiftTypeGroup shiftTypeGroup = null;
        if (shiftTypeCreationModel.getShiftTypeGroupId() > 0) {
            shiftTypeGroup = this.shiftTypeGroupRepository.findOne(shiftTypeCreationModel.getShiftTypeGroupId());
            if (shiftTypeGroup != null) {
                if (!shiftTypeGroup.addShiftType(shiftType)) {
                    FieldError fieldError = new FieldError("shiftTypeGroup", "shiftTypeGroup", "error.shiftTypeGroup.shiftTypeCollision");
                    throw new CustomFieldError("shiftTypeGroup", fieldError);
                }
            }
        }

        shiftType.setShiftTypeGroup(shiftTypeGroup);
    }

    @Override
    public void setNotActive(Long shiftTypeId) {
        ShiftType shiftType = this.shiftTypeRepository.findOne(shiftTypeId);
        shiftType.setIsActive(false);
    }

    @Override
    public void delete(Long shiftTypeId) {
        this.shiftTypeRepository.delete(shiftTypeId);
    }

    @Override
    public List<ShiftTypeInWorkShiftViewModel> getRegularAndActiveShiftTypes() {
        List<ShiftType> shiftTypes = this.shiftTypeRepository.findAllByIsActiveAndIsRegularShiftOrderByIsRegularShiftDescShortNameAsc(true, true);
        return this.convertListToShiftTypeInWorkShiftViewModel(shiftTypes);
    }

    @Override
    public List<ShiftTypeInWorkShiftViewModel> getRegularAndActiveShiftTypesByBusinessUnit(Long businessUnitId) {
        List<ShiftType> shiftTypes = this.shiftTypeRepository.findAllByIsActiveAndIsRegularShiftOrderByIsRegularShiftDescShortNameAsc(true, true);
        WorkScheduleSettingsViewModel sr = this.workScheduleSettingsService.getScheduleSettings(businessUnitId);
        List<ShiftType> result = new ArrayList<>();
        for (ShiftType shiftType : shiftTypes) {
            if ((sr.getIsOnDoubleShiftRegime() && shiftType.getCanBeOnDoubleShiftRegime() && !result.contains(shiftType)) ||
                    (sr.getIsOnTripleShiftRegime() && shiftType.getCanBeOnTripleShiftRegime() && !result.contains(shiftType))) {
                result.add(shiftType);
            }
        }
        return this.convertListToShiftTypeInWorkShiftViewModel(result);
    }

    @Override
    public List<ShiftTypeInWorkShiftViewModel> getNotRegularAndActiveShiftTypes() {
        List<ShiftType> shiftTypes = this.shiftTypeRepository.findAllByIsActiveAndIsRegularShiftOrderByIsRegularShiftDescShortNameAsc(true, false);
        return this.convertListToShiftTypeInWorkShiftViewModel(shiftTypes);
    }

    @Override
    public List<ShiftTypeWorkScheduleSettingsViewModel> getAllShiftTypeWorkScheduleSettingsViewModel() {
        List<ShiftType> shiftTypes = this.shiftTypeRepository.findAllByIsActiveAndIsRegularShiftOrderByIsRegularShiftDescShortNameAsc(true, true);
        List<ShiftTypeWorkScheduleSettingsViewModel> viewModels = new ArrayList<>();
        for (ShiftType shiftType : shiftTypes) {
            ShiftTypeWorkScheduleSettingsViewModel viewModel = new ShiftTypeWorkScheduleSettingsViewModel();
            viewModel.setId(shiftType.getId());
            viewModel.setFullName(shiftType.getFullName());
            viewModel.setIsInSettings(false);

            viewModels.add(viewModel);
        }

        return viewModels;
    }

    @Override
    public ShiftTypeViewModel getShiftTypeViewModel(ShiftType shiftType) {
        return this.convertToShiftTypeViewModel(shiftType);
    }

    @Autowired
    public void setShiftTypeRepository(ShiftTypeRepository shiftTypeRepository) {
        this.shiftTypeRepository = shiftTypeRepository;
    }

    @Autowired
    public void setAnnualLeaveTypeRepository(AnnualLeaveTypeRepository annualLeaveTypeRepository) {
        this.annualLeaveTypeRepository = annualLeaveTypeRepository;
    }

    @Autowired
    public void setShiftTypeGroupRepository(ShiftTypeGroupRepository shiftTypeGroupRepository) {
        this.shiftTypeGroupRepository = shiftTypeGroupRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setWorkScheduleSettingsService(WorkScheduleSettingsService workScheduleSettingsService) {
        this.workScheduleSettingsService = workScheduleSettingsService;
    }

    private boolean isOccupied(AnnualLeaveType annualLeaveType, ShiftType shiftType, Long id) {
        if (annualLeaveType != null && annualLeaveType.getId() != id) {
            return true;
        }

        if (shiftType != null && shiftType.getId() != id) {
            return true;
        }

        return false;
    }

    private List<ShiftTypeInWorkShiftViewModel> convertListToShiftTypeInWorkShiftViewModel(List<ShiftType> shiftTypes) {
        List<ShiftTypeInWorkShiftViewModel> viewModels = new ArrayList<>();
        for (ShiftType shiftType : shiftTypes) {
            viewModels.add(this.convertToShiftTypeInWorkShiftViewModel(shiftType));
        }

        return viewModels;
    }

    private ShiftTypeInWorkShiftViewModel convertToShiftTypeInWorkShiftViewModel(ShiftType shiftType) {
        ShiftTypeInWorkShiftViewModel viewModel = new ShiftTypeInWorkShiftViewModel();
        viewModel.setId(shiftType.getId());
        viewModel.setIsEndOnNextDay(shiftType.getIsEndOnNextDay());
        viewModel.setIsRegularShift(shiftType.getIsRegularShift());
        viewModel.setFullName(shiftType.getFullName());
        viewModel.setShortName(shiftType.getShortName());
        String shiftDuration = "";
        if (shiftType.getShiftDuration() != null) {
            shiftDuration = TimeCalculator.getTimeAsString(shiftType.getShiftDuration());
        }
        viewModel.setShiftDuration(shiftDuration);

        String breakDuration = "";
        if (shiftType.getBreakDuration() != null) {
            breakDuration = TimeCalculator.getTimeAsString(shiftType.getBreakDuration());
        }
        viewModel.setBreakDuration(breakDuration);

        String startTime = "";
        if (shiftType.getStartTime() != null) {
            startTime = TimeCalculator.getTimeAsString(shiftType.getStartTime());
        }
        viewModel.setStartTime(startTime);

        String endTime = "";
        if (shiftType.getEndTime() != null) {
            endTime = TimeCalculator.getTimeAsString(shiftType.getEndTime());
        }
        viewModel.setEndTime(endTime);

        String secondStartTime = "";
        if (shiftType.getSecondStartTime() != null) {
            secondStartTime = TimeCalculator.getTimeAsString(shiftType.getSecondStartTime());
        }
        viewModel.setSecondStartTime(secondStartTime);

        String secondEndTime = "";
        if (shiftType.getSecondEndTime() != null) {
            secondEndTime = TimeCalculator.getTimeAsString(shiftType.getSecondEndTime());
        }
        viewModel.setSecondEndTime(secondEndTime);

        viewModel.setCanBeOnDoubleShiftRegime(shiftType.getCanBeOnDoubleShiftRegime());
        viewModel.setCanBeOnTripleShiftRegime(shiftType.getCanBeOnTripleShiftRegime());
        viewModel.setCanBeOnWorkDay(shiftType.getCanBeOnWorkDay());
        viewModel.setCanBeOnRestDay(shiftType.getCanBeOnRestDay());
        viewModel.setCanBeOnLastWorkDayBeforeRestDay(shiftType.getCanBeOnLastWorkDayBeforeRestDay());
        viewModel.setCanBeOnLastRestDayBeforeWorkDay(shiftType.getCanBeOnLastRestDayBeforeWorkDay());

        return viewModel;
    }

    private ShiftTypeViewModel convertToShiftTypeViewModel(ShiftType shiftType) {
        ShiftTypeViewModel viewModel = new ShiftTypeViewModel();
        viewModel.setId(shiftType.getId());
        viewModel.setFullName(shiftType.getFullName());
        viewModel.setShortName(shiftType.getShortName());
        viewModel.setShiftDuration(shiftType.getShiftDuration());
        viewModel.setBreakDuration(shiftType.getBreakDuration());
        viewModel.setStartTime(shiftType.getStartTime());
        viewModel.setEndTime(shiftType.getEndTime());
        viewModel.setSecondStartTime(shiftType.getSecondStartTime());
        viewModel.setSecondEndTime(shiftType.getSecondEndTime());
        viewModel.setIsRegularShift(shiftType.getIsRegularShift());
        viewModel.setIsEndOnNextDay(shiftType.getIsEndOnNextDay());
        viewModel.setCanBeOnWorkDay(shiftType.getCanBeOnWorkDay());
        viewModel.setCanBeOnRestDay(shiftType.getCanBeOnRestDay());
        viewModel.setCanBeOnDoubleShiftRegime(shiftType.getCanBeOnDoubleShiftRegime());
        viewModel.setCanBeOnTripleShiftRegime(shiftType.getCanBeOnTripleShiftRegime());
        //viewModel.setCanBeOnFirstWorkDayAfterRestDay(shiftType.getCanBeOnFirstWorkDayAfterRestDay());
        //viewModel.setCanBeOnFirstRestDayAfterWorkDay(shiftType.getCanBeOnFirstRestDayAfterWorkDay());
        viewModel.setCanBeOnLastWorkDayBeforeRestDay(shiftType.getCanBeOnLastWorkDayBeforeRestDay());
        viewModel.setCanBeOnLastRestDayBeforeWorkDay(shiftType.getCanBeOnLastRestDayBeforeWorkDay());
        Long groupId = 0L;
        if (shiftType.getShiftTypeGroup() != null) {
            groupId = shiftType.getShiftTypeGroup().getId();
        }
        viewModel.setShiftTypeGroupId(groupId);

        return viewModel;
    }
}
