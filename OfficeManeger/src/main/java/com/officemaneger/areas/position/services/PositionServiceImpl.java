package com.officemaneger.areas.position.services;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import com.officemaneger.areas.businessUnit.repositories.BusinessUnitRepository;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.models.viewModels.EmployeeFullNameViewModel;
import com.officemaneger.areas.employee.repositories.EmployeeRepository;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.position.entities.Position;
import com.officemaneger.areas.position.models.bindingModels.PositionAddNewModel;
import com.officemaneger.areas.position.models.bindingModels.PositionCreationModel;
import com.officemaneger.areas.position.models.viewModels.PositionNameViewModel;
import com.officemaneger.areas.position.models.viewModels.PositionViewInEditBusinessUnitModel;
import com.officemaneger.areas.position.repositories.PositionRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class PositionServiceImpl implements PositionService {

    private PositionRepository positionRepository;

    private BusinessUnitRepository businessUnitRepository;

    private EmployeeService employeeService;

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    public PositionServiceImpl() {
    }

    @Override
    public Long create(PositionCreationModel positionCreationModel) {
        Position position = this.modelMapper.map(positionCreationModel, Position.class);
        Position savedPosition = this.positionRepository.save(position);
        return savedPosition.getId();
    }

    @Override
    public List<PositionNameViewModel> getAllPositionNamesViewModels() {
        List<Position> activePositions = this.positionRepository.findAll();
        List<PositionNameViewModel> resultList = new ArrayList<>();
        for (Position position : activePositions) {
            PositionNameViewModel positionNameViewModel = this.modelMapper.map(position, PositionNameViewModel.class);
            resultList.add(positionNameViewModel);
        }

        return resultList;
    }

    @Override
    public PositionNameViewModel getPositionNameViewModel(Long id) {
        Position position = this.positionRepository.getOne(id);
        if (position == null) {
            return null;
        }

        return this.modelMapper.map(position, PositionNameViewModel.class);
    }

    @Override
    public List<String> getAllPositionNames() {
        List<Position> activePositions = this.positionRepository.findAll();
        List<String> resultList = new ArrayList<>();
        for (Position position : activePositions) {
            resultList.add(position.getName());
        }

        return resultList;
    }

    @Override
    public boolean isNameOccupied(String name) {
        Position position = this.positionRepository.findOneByName(name);
        return position != null;
    }

    @Override
    public PositionViewInEditBusinessUnitModel getPositionViewInEditBusinessUnitModel(Long id) {
        Position position = this.positionRepository.findOne(id);
        if (position != null) {
            PositionViewInEditBusinessUnitModel positionViewModel = new PositionViewInEditBusinessUnitModel();
            positionViewModel.setId(position.getId());
            positionViewModel.setName(position.getName());

            EmployeeFullNameViewModel employee = null;
            if (position.getEmployee() != null) {
                employee = this.employeeService.getEmployeeFullNameViewModel(position.getEmployee().getId());
            }

            positionViewModel.setEmployee(employee);

            return positionViewModel;
        }

        return null;
    }

    @Override
    public void delete(long positionId) {
        Position position = this.positionRepository.findOne(positionId);
        if (position.getEmployee() != null) {
            position.getEmployee().setPosition(null);
            position.setEmployee(null);
        }
        this.positionRepository.delete(positionId);
    }

    @Override
    public PositionNameViewModel addNewPosition(PositionAddNewModel positionAddNewModel) {
        Position position = new Position();
        position.setName("temp name");
        BusinessUnit businessUnit = this.businessUnitRepository.findOne(positionAddNewModel.getBusinessUnitId());
        if (businessUnit == null) {
            return null;
        }

        Position savedPosition = this.positionRepository.save(position);
        savedPosition.setBusinessUnit(businessUnit);
        businessUnit.addEmployeePosition(savedPosition);

        PositionNameViewModel viewModel = new PositionNameViewModel();
        viewModel.setId(savedPosition.getId());

        return viewModel;
    }

    @Override
    public void updatePositionName(String newPositionName, Long positionId) {
        Position position = this.positionRepository.findOne(positionId);
        position.setName(newPositionName);
    }

    @Override
    public void updatePositionEmployee(Long positionId, Long newEmployeeId) {
        Position position = this.positionRepository.findOne(positionId);
        if (newEmployeeId == null || newEmployeeId == 0L) {
            if (position.getEmployee() != null) {
                position.getEmployee().setPosition(null);
            }
            position.setEmployee(null);
        } else {
            Employee employee = this.employeeRepository.findOne(newEmployeeId);
            if (position.getEmployee() != null) {
                position.getEmployee().setPosition(null);
            }
            position.setEmployee(employee);
            employee.setPosition(position);
        }
    }

    @Autowired
    public void setPositionRepository(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Autowired
    public void setBusinessUnitRepository(BusinessUnitRepository businessUnitRepository) {
        this.businessUnitRepository = businessUnitRepository;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
}
