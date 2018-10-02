package com.officemaneger.areas.businessUnit.services;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import com.officemaneger.areas.businessUnit.models.bindingModels.BusinessUnitCreationModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitEditViewModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitNameViewModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitViewModel;
import com.officemaneger.areas.businessUnit.repositories.BusinessUnitRepository;
import com.officemaneger.areas.computers.entities.Computer;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.repositories.EmployeeRepository;
import com.officemaneger.areas.employee.services.EmployeeService;
import com.officemaneger.areas.position.entities.Position;
import com.officemaneger.areas.position.models.bindingModels.PositionCreationModel;
import com.officemaneger.areas.position.models.viewModels.PositionViewInEditBusinessUnitModel;
import com.officemaneger.areas.position.repositories.PositionRepository;
import com.officemaneger.areas.position.services.PositionService;
import com.officemaneger.areas.user.services.UserService;
import com.officemaneger.areas.workScheduleSettings.entities.WorkScheduleSettings;
import com.officemaneger.areas.workScheduleSettings.services.WorkScheduleSettingsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class BusinessUnitServiceImpl implements BusinessUnitService {

    private BusinessUnitRepository businessUnitRepository;

    private PositionRepository positionRepository;

    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    private UserService userService;

    private PositionService positionService;

    private ModelMapper modelMapper;

    private WorkScheduleSettingsService workScheduleSettingsService;

    public BusinessUnitServiceImpl() {
    }

    @Override
    public void create(BusinessUnitCreationModel businessUnitCreationModel) {
        BusinessUnit businessUnit = new BusinessUnit(businessUnitCreationModel.getUnitName());

        if (!businessUnitCreationModel.getMainUnit().isEmpty()) {
            BusinessUnit mainUnit = this.businessUnitRepository.findOneByUnitName(businessUnitCreationModel.getMainUnit());
            businessUnit.setMainUnit(mainUnit);
            mainUnit.addSubUnit(businessUnit);
        } else {
            businessUnit.setMainUnit(null);
        }

        //bossPosition
        if (!businessUnitCreationModel.getBossPosition().isEmpty()) {
            PositionCreationModel bossPositionCreationModel = new PositionCreationModel(businessUnitCreationModel.getBossPosition());
            Long bossPositionId = this.positionService.create(bossPositionCreationModel);
            Position bossPosition = this.positionRepository.findOne(bossPositionId);
            businessUnit.setBossPosition(bossPosition);
            bossPosition.setBusinessUnit(businessUnit);

            if (businessUnitCreationModel.getBossId() > 0) {
                Employee boss = this.employeeRepository.findOne(businessUnitCreationModel.getBossId());
                boss.setPosition(bossPosition);
                bossPosition.setEmployee(boss);
            } else {
                bossPosition.setEmployee(null);
            }
        } else {
            businessUnit.setBossPosition(null);
        }

        //employees
        businessUnit.setPositions(new ArrayList<>());
        int numbOfEmployees = businessUnitCreationModel.getEmployeesPositions().size();
        for (int i = 0; i < numbOfEmployees; i++) {
            if (!businessUnitCreationModel.getEmployeesPositions().get(i).isEmpty()) {
                PositionCreationModel emplPositionCreationModel = new PositionCreationModel(businessUnitCreationModel.getEmployeesPositions().get(i));
                Long emplPositionId = this.positionService.create(emplPositionCreationModel);
                Position emplPosition = this.positionRepository.findOne(emplPositionId);
                businessUnit.addEmployeePosition(emplPosition);
                emplPosition.setBusinessUnit(businessUnit);

                if (businessUnitCreationModel.getEmployeesIds().get(i) > 0) {
                    Employee employee = this.employeeRepository.findOne(businessUnitCreationModel.getEmployeesIds().get(i));
                    employee.setPosition(emplPosition);
                    emplPosition.setEmployee(employee);
                } else {
                    emplPosition.setEmployee(null);
                }
            }
        }

        this.businessUnitRepository.save(businessUnit);

        //create work schedule settings too
        WorkScheduleSettings workScheduleSettings = this.workScheduleSettingsService.creteWorkScheduleSettings(businessUnit);
        businessUnit.setWorkScheduleSettings(workScheduleSettings);
    }

    @Override
    public List<BusinessUnitNameViewModel> getAllBusinessUnitNameViewModels() {
        List<BusinessUnit> businessUnits = this.businessUnitRepository.findAll();
        List<BusinessUnitNameViewModel> businessUnitNameViewModels = new ArrayList<>();
        for (BusinessUnit businessUnit : businessUnits) {
            BusinessUnitNameViewModel businessUnitNameViewModel = this.modelMapper.map(businessUnit, BusinessUnitNameViewModel.class);
            businessUnitNameViewModels.add(businessUnitNameViewModel);
        }

        return businessUnitNameViewModels;
    }

    @Override
    public List<BusinessUnitViewModel> getAllBusinessUnitViewModels() {
        List<BusinessUnit> businessUnits = this.businessUnitRepository.findAll();
        List<BusinessUnitViewModel> businessUnitViewModels = new ArrayList<>();
        for (BusinessUnit businessUnit : businessUnits) {
            BusinessUnitViewModel businessUnitViewModel = this.modelMapper.map(businessUnit, BusinessUnitViewModel.class);
            if (businessUnit.getMainUnit() != null) {
                businessUnitViewModel.setMainUnit(businessUnit.getMainUnit().getUnitName());
            }

            if (businessUnit.getBossPosition() != null && businessUnit.getBossPosition().getEmployee() != null) {
                String bossNames = businessUnit.getBossPosition().getEmployee().getFullName();
                businessUnitViewModel.setBoss(bossNames);
            } else {
                businessUnitViewModel.setBoss(null);
            }

            businessUnitViewModels.add(businessUnitViewModel);
        }

        return businessUnitViewModels;
    }

    @Override
    public Long getMainUnitIdByUnitId(Long businessUnitId) {
        return this.businessUnitRepository.getMainUnitIdByUnitId(businessUnitId);
    }

    @Override
    public BusinessUnitNameViewModel getBusinessUnitViewModel(Long id) {
        BusinessUnit businessUnit = this.businessUnitRepository.findOne(id);
        if (businessUnit == null) {
            return null;
        }

        return this.modelMapper.map(businessUnit, BusinessUnitNameViewModel.class);
    }

    @Override
    public List<String> getAllBusinessUnitNames() {
        List<BusinessUnit> businessUnits = this.businessUnitRepository.findAll();
        List<String> businessUnitsNames = new ArrayList<>();
        for (BusinessUnit businessUnit : businessUnits) {
            businessUnitsNames.add(businessUnit.getUnitName());
        }

        return businessUnitsNames;
    }

    @Override
    public BusinessUnitNameViewModel findOneByUnitName(String unitName) {
        BusinessUnit businessUnit = this.businessUnitRepository.findOneByUnitName(unitName);
        if (businessUnit == null) {
            return null;
        }

        return this.modelMapper.map(businessUnit, BusinessUnitNameViewModel.class);
    }

    @Override
    public boolean isNameOccupied(String unitName) {
        BusinessUnit businessUnit = this.businessUnitRepository.findOneByUnitName(unitName);
        return businessUnit != null;
    }

    @Override
    public boolean isNameOccupied(String newUnitName, Long unitId) {
        BusinessUnit nameBusinessUnit = this.businessUnitRepository.findOneByUnitName(newUnitName);
        if (nameBusinessUnit != null && nameBusinessUnit.getId() != unitId) {
            return true;
        }

        return false;
    }

    @Override
    public void delete(Long id) {
        BusinessUnit businessUnit = this.businessUnitRepository.findOne(id);
        if (businessUnit.getBossPosition() != null) {
            Employee boss = businessUnit.getBossPosition().getEmployee();
            if (boss != null) {
                boss.setPosition(null);
            }
        }

        List<Position> emplPositions = businessUnit.getPositions();
        for (Position emplPosition : emplPositions) {
            Employee employee = emplPosition.getEmployee();
            if (employee != null) {
                employee.setPosition(null);
            }
        }
        BusinessUnit mainUnit = businessUnit.getMainUnit();
        if (mainUnit != null) {
            List<BusinessUnit> units = mainUnit.getSubUnits();
            units.remove(businessUnit);
        }
        businessUnit.setMainUnit(null);

        List<BusinessUnit> subUnits = businessUnit.getSubUnits();
        for (BusinessUnit subUnit : subUnits) {
            subUnit.setMainUnit(null);
        }
        businessUnit.setSubUnits(new ArrayList<>());

        List<Computer> computers = businessUnit.getComputers();
        for (Computer computer : computers) {
            computer.setBusinessUnit(null);
        }

        this.businessUnitRepository.delete(id);
    }

    @Override
    public BusinessUnitEditViewModel getBusinessUnitEditViewModel(Long id) {
        BusinessUnit businessUnit = this.businessUnitRepository.findOne(id);
        if (businessUnit == null) {
            return null;
        }

        BusinessUnitEditViewModel businessUnitEditViewModel = new BusinessUnitEditViewModel();
        businessUnitEditViewModel.setId(id);
        businessUnitEditViewModel.setUnitName(businessUnit.getUnitName());

        //mainUnit
        Long mainUnitId = 0L;
        if (businessUnit.getMainUnit() != null) {
            mainUnitId = businessUnit.getMainUnit().getId();
        }
        businessUnitEditViewModel.setMainUnitId(mainUnitId);

        //boss
        PositionViewInEditBusinessUnitModel bossPosition = null;
        Position dbBossPosition = null;
        if (businessUnit.getBossPosition() != null) {
            dbBossPosition = businessUnit.getBossPosition();
            bossPosition = this.positionService.getPositionViewInEditBusinessUnitModel(dbBossPosition.getId());
        }
        businessUnitEditViewModel.setBossPosition(bossPosition);

        //employees
        List<Position> positionsList = businessUnit.getPositions();
        List<PositionViewInEditBusinessUnitModel> emplPositions = new ArrayList<>();
        for (Position position : positionsList) {
            if (dbBossPosition != null && dbBossPosition.equals(position)) {
                continue;
            }
            PositionViewInEditBusinessUnitModel emplPosition = this.positionService.getPositionViewInEditBusinessUnitModel(position.getId());
            emplPositions.add(emplPosition);
        }

        businessUnitEditViewModel.setPositions(emplPositions);

        return businessUnitEditViewModel;
    }

    @Override
    public void updateUnitName(Long unitId, String newUnitName) {
        BusinessUnit unit = this.businessUnitRepository.findOne(unitId);
        if (unit != null) {
            unit.setUnitName(newUnitName);
        }
    }

    @Override
    public void updateMainUnit(Long unitId, Long mainUnitId) {
        BusinessUnit unit = this.businessUnitRepository.findOne(unitId);
        if (unit != null) {
            BusinessUnit mainUnit = this.businessUnitRepository.findOne(mainUnitId);
            BusinessUnit oldMainUnit = unit.getMainUnit();
            if (oldMainUnit != null) {
                oldMainUnit.removeSubUnit(unit);
            }

            unit.setMainUnit(mainUnit);
            if (mainUnit != null) {
                mainUnit.addSubUnit(unit);
            }
        }
    }

    @Override
    public List<Long> getSubUnitsIds(Long businessUnitId) {
        return this.businessUnitRepository.getSubUnitIdsByBusinessUnitId(businessUnitId);
    }

    @Override
    public Long getPermanentBossIdByBusinessUnit(Long businessUnitId) {
        return this.businessUnitRepository.getBossIdByBusinessUnitId(businessUnitId);
    }

    @Override
    public List<Long> getBossesIdsByBusinessUnit(Long businessUnitId) {
        List<Long> allBossesIds = new ArrayList<>();
        Long bossId = this.businessUnitRepository.getBossIdByBusinessUnitId(businessUnitId);
        if (bossId != null) {
            allBossesIds.add(bossId);
        }

        List<Long> employeesIds = this.businessUnitRepository.getEmployeesIdsByBusinessUnitId(businessUnitId);
        for (Long employeeId : employeesIds) {
            if (this.userService.hasEmployeeBossRole(employeeId)) {
                allBossesIds.add(employeeId);
            }
        }

        return allBossesIds;
    }

    @Override
    public int getNumbOfPositionsInBusinessUnit(Long businessUnitId) {
        return this.businessUnitRepository.getNumbOfEmployeePositionsInBusinessUnit(businessUnitId) + 1;
    }

    @Override
    public int getNumbOfEmployeePositionsInBusinessUnit(Long businessUnitId) {
        return this.businessUnitRepository.getNumbOfEmployeePositionsInBusinessUnit(businessUnitId);
    }

    @Override
    public List<Long> getBusinessUnitEmployeesIds(Long businessUnitId) {
        List<Long> employeeIds = new ArrayList<>();

        return this.getBusinessUnitAndSubUnitEmployeesIds(employeeIds, businessUnitId);
    }

    @Override
    public String getBusinessUnitName(Long businessUnitId) {
        return this.businessUnitRepository.getBusinessUnitName(businessUnitId);
    }

    @Autowired
    public void setBusinessUnitRepository(BusinessUnitRepository businessUnitRepository) {
        this.businessUnitRepository = businessUnitRepository;
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    public void setPositionRepository(PositionRepository positionRepository) {
        this.positionRepository = positionRepository;
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setWorkScheduleSettingsService(WorkScheduleSettingsService workScheduleSettingsService) {
        this.workScheduleSettingsService = workScheduleSettingsService;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setPositionService(PositionService positionService) {
        this.positionService = positionService;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    private List<Long> getBusinessUnitAndSubUnitEmployeesIds(List<Long> employeeIds, Long businessUnitId) {
        Long boosId = this.businessUnitRepository.getBossIdByBusinessUnitId(businessUnitId);
        if (boosId != null) {
            employeeIds.add(boosId);
        }

        employeeIds.addAll(this.businessUnitRepository.getEmployeesIdsByBusinessUnitId(businessUnitId));

        List<Long> subUnitsIds = this.businessUnitRepository.getSubUnitIdsByBusinessUnitId(businessUnitId);
        for (Long subUnitId : subUnitsIds) {
            this.getBusinessUnitAndSubUnitEmployeesIds(employeeIds, subUnitId);
        }

        return employeeIds;
    }

}




