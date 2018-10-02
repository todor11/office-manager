package com.officemaneger.areas.annualLeaveType.services;

import com.officemaneger.areas.annualLeaveType.models.bindingModels.AnnualLeaveTypeCreationModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeFullNameViewModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeViewModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeWorkShiftViewModel;

import java.util.List;

public interface AnnualLeaveTypeService {

    AnnualLeaveTypeViewModel create(AnnualLeaveTypeCreationModel annualLeaveTypeCreationModel);

    List<AnnualLeaveTypeFullNameViewModel> getAllActiveAnnualLeaveTypesFullNames();

    List<AnnualLeaveTypeViewModel> getAllActiveAnnualLeaveTypes();

    List<String> getAllActiveAnnualLeaveTypeFullNames();

    List<String> getAllActiveAnnualLeaveTypeShortNames();

    boolean isFullNameOccupied(String fullName);

    boolean isFullNameOccupied(String fullName, Long id);

    boolean isShortNameOccupied(String shortName);

    boolean isShortNameOccupied(String shortName, Long id);

    void update(Long annualLeaveTypeId, AnnualLeaveTypeCreationModel annualLeaveTypeCreationModel);

    void setNotActive(Long annualLeaveTypeId);

    void delete(Long annualLeaveTypeId);

    List<String> getActiveAnnualLeaveTypesAsString();

    boolean isAnnualLeaveType(Long dualNameTypeId);

    List<AnnualLeaveTypeWorkShiftViewModel> getAllActiveAnnualLeaveTypesInWorkShift();
}
