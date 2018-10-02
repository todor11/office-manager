package com.officemaneger.areas.annualLeaveType.services;

import com.officemaneger.areas.annualLeave.services.AnnualLeaveService;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.annualLeaveType.models.bindingModels.AnnualLeaveTypeCreationModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeFullNameViewModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeViewModel;
import com.officemaneger.areas.annualLeaveType.models.viewModels.AnnualLeaveTypeWorkShiftViewModel;
import com.officemaneger.areas.annualLeaveType.repositories.AnnualLeaveTypeRepository;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftType.repositories.ShiftTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class AnnualLeaveTypeServiceImpl implements AnnualLeaveTypeService {

    private AnnualLeaveTypeRepository annualLeaveTypeRepository;

    private AnnualLeaveService annualLeaveService;

    private ShiftTypeRepository shiftTypeRepository;

    private ModelMapper modelMapper;

    private EmployeeService employeeService;

    public AnnualLeaveTypeServiceImpl() {
    }

    @Override
    public AnnualLeaveTypeViewModel create(AnnualLeaveTypeCreationModel annualLeaveTypeCreationModel) {
        AnnualLeaveType annualLeaveType = this.modelMapper.map(annualLeaveTypeCreationModel, AnnualLeaveType.class);
        annualLeaveType.setIsActive(true);

        AnnualLeaveType savedAnnualLeaveType = this.annualLeaveTypeRepository.save(annualLeaveType);
        AnnualLeaveTypeViewModel annualLeaveTypeViewModel = this.modelMapper.map(savedAnnualLeaveType, AnnualLeaveTypeViewModel.class);

        return annualLeaveTypeViewModel;
    }

    @Override
    public List<AnnualLeaveTypeFullNameViewModel> getAllActiveAnnualLeaveTypesFullNames() {
        List<AnnualLeaveType> activeAnnualLeaveTypes = this.annualLeaveTypeRepository.findAllByIsActive(true);
        List<AnnualLeaveTypeFullNameViewModel> resultList = new ArrayList<>();
        for (AnnualLeaveType activeAnnualLeaveType : activeAnnualLeaveTypes) {
            AnnualLeaveTypeFullNameViewModel annualLeaveTypeView = this.modelMapper.map(activeAnnualLeaveType, AnnualLeaveTypeFullNameViewModel.class);
            resultList.add(annualLeaveTypeView);
        }

        return resultList;
    }

    @Override
    public List<AnnualLeaveTypeViewModel> getAllActiveAnnualLeaveTypes() {
        List<AnnualLeaveType> activeAnnualLeaveTypes = this.annualLeaveTypeRepository.findAllByIsActive(true);
        List<AnnualLeaveTypeViewModel> resultList = new ArrayList<>();
        for (AnnualLeaveType activeAnnualLeaveType : activeAnnualLeaveTypes) {
            AnnualLeaveTypeViewModel annualLeaveTypeView = this.modelMapper.map(activeAnnualLeaveType, AnnualLeaveTypeViewModel.class);
            resultList.add(annualLeaveTypeView);
        }

        return resultList;
    }

    @Override
    public List<AnnualLeaveTypeWorkShiftViewModel> getAllActiveAnnualLeaveTypesInWorkShift() {
        List<AnnualLeaveType> activeAnnualLeaveTypes = this.annualLeaveTypeRepository.findAllByIsActive(true);
        List<AnnualLeaveTypeWorkShiftViewModel> resultList = new ArrayList<>();
        for (AnnualLeaveType activeAnnualLeaveType : activeAnnualLeaveTypes) {
            AnnualLeaveTypeWorkShiftViewModel annualLeaveTypeView = new AnnualLeaveTypeWorkShiftViewModel();
            annualLeaveTypeView.setId(activeAnnualLeaveType.getId());
            annualLeaveTypeView.setFullName(activeAnnualLeaveType.getFullName());
            annualLeaveTypeView.setShortName(activeAnnualLeaveType.getShortName());
            List<Long> employeeIds = this.employeeService.getActiveEmployeesIds();
            annualLeaveTypeView.setEmployeeIds(employeeIds);
            List<Long> annualLeaveIds = this.annualLeaveService.getAnnualLeaveIdsByEmployeeIds(employeeIds, activeAnnualLeaveType.getId());
            annualLeaveTypeView.setAnnualLeaveIds(annualLeaveIds);

            resultList.add(annualLeaveTypeView);
        }

        return resultList;
    }

    @Override
    public List<String> getAllActiveAnnualLeaveTypeFullNames() {
        List<AnnualLeaveType> activeAnnualLeaveTypes = this.annualLeaveTypeRepository.findAllByIsActive(true);
        List<String> resultList = new ArrayList<>();
        for (AnnualLeaveType activeAnnualLeaveType : activeAnnualLeaveTypes) {
            resultList.add(activeAnnualLeaveType.getFullName());
        }

        return resultList;
    }

    @Override
    public List<String> getAllActiveAnnualLeaveTypeShortNames() {
        List<AnnualLeaveType> activeAnnualLeaveTypes = this.annualLeaveTypeRepository.findAllByIsActive(true);
        List<String> resultList = new ArrayList<>();
        for (AnnualLeaveType activeAnnualLeaveType : activeAnnualLeaveTypes) {
            resultList.add(activeAnnualLeaveType.getShortName());
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
    public boolean isFullNameOccupied(String fullName, Long id) {
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOneByFullName(fullName);
        ShiftType shiftType = this.shiftTypeRepository.findOneByFullName(fullName);
        if (annualLeaveType != null && annualLeaveType.getId() != id) {
            return true;
        }

        if (shiftType != null && shiftType.getId() != id) {
            return true;
        }

        return false;
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
        if (annualLeaveType != null && annualLeaveType.getId() != id) {
            return true;
        }

        if (shiftType != null && shiftType.getId() != id) {
            return true;
        }

        return false;
    }

    @Override
    public void update(Long annualLeaveTypeId, AnnualLeaveTypeCreationModel annualLeaveTypeCreationModel) {
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOne(annualLeaveTypeId);
        annualLeaveType.setFullName(annualLeaveTypeCreationModel.getFullName());
        annualLeaveType.setShortName(annualLeaveTypeCreationModel.getShortName());
    }

    @Override
    public void setNotActive(Long annualLeaveTypeId) {
        AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOne(annualLeaveTypeId);
        annualLeaveType.setIsActive(false);
    }

    @Override
    public void delete(Long annualLeaveTypeId) {
        boolean hasAnnualLeave = this.annualLeaveService.hasAnnualLeaveType(annualLeaveTypeId);
        if (hasAnnualLeave) {
            AnnualLeaveType annualLeaveType = this.annualLeaveTypeRepository.findOne(annualLeaveTypeId);
            annualLeaveType.setIsActive(false);
        } else {
            this.annualLeaveTypeRepository.delete(annualLeaveTypeId);
        }
    }

    @Override
    public List<String> getActiveAnnualLeaveTypesAsString() {
        return this.annualLeaveTypeRepository.getAnnualLeaveTypeFullNameByIsActiveOrdered(true);
    }

    @Override
    public boolean isAnnualLeaveType(Long dualNameTypeId) {
        Long annualLeaveTypeId = this.annualLeaveTypeRepository.getAnnualLeaveTypeIdByDualNameTypeId(dualNameTypeId);
        if (annualLeaveTypeId == null) {
            return false;
        }

        return true;
    }

    @Autowired
    public void setAnnualLeaveTypeRepository(AnnualLeaveTypeRepository annualLeaveTypeRepository) {
        this.annualLeaveTypeRepository = annualLeaveTypeRepository;
    }

    @Autowired
    public void setShiftTypeRepository(ShiftTypeRepository shiftTypeRepository) {
        this.shiftTypeRepository = shiftTypeRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setAnnualLeaveService(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }
}
