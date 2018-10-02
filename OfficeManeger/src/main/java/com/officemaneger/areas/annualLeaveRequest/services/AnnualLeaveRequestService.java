package com.officemaneger.areas.annualLeaveRequest.services;

import com.officemaneger.areas.annualLeaveDateInterval.models.viewModels.AnnualLeaveDateIntervalViewModel;
import com.officemaneger.areas.annualLeaveRequest.models.bindingModels.AnnualLeaveRequestUpdateModel;
import com.officemaneger.areas.annualLeaveRequest.models.bindingModels.AnnualLeaveRequestUserCreationModel;
import com.officemaneger.areas.annualLeaveRequest.models.viewModels.AnnualLeaveRequestUpdateViewModel;
import com.officemaneger.areas.annualLeaveRequest.models.viewModels.AnnualLeaveRequestViewModel;

import java.util.List;

public interface AnnualLeaveRequestService {

    boolean userCreateRequest(AnnualLeaveRequestUserCreationModel requestCreationModel);

    List<AnnualLeaveRequestViewModel> bossGetRequests(Long bossId);

    Long getEmployeeIdByRequestId(Long requestId);

    void deleteRequest(Long requestId);

    List<AnnualLeaveDateIntervalViewModel> executeEmployeeRequest(Long requestId);

    boolean isEmployeeOwnerOfRequest(Long employeeId, Long requestId);

    AnnualLeaveRequestUpdateViewModel employeeGetUpdateRequestModel(Long employeeId, Long requestId);

    boolean userUpdateRequest(AnnualLeaveRequestUpdateModel annualLeaveRequestModel);
}
