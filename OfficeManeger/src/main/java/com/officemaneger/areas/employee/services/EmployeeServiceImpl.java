package com.officemaneger.areas.employee.services;

import com.officemaneger.areas.annualLeave.entities.AnnualLeave;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveEmployeeViewModel;
import com.officemaneger.areas.annualLeave.models.viewModels.AnnualLeaveWorkShiftViewModel;
import com.officemaneger.areas.annualLeave.services.AnnualLeaveService;
import com.officemaneger.areas.businessUnit.services.BusinessUnitService;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.models.bindingModels.CreateEmployeeModel;
import com.officemaneger.areas.employee.models.bindingModels.EmployeeUpdateModel;
import com.officemaneger.areas.employee.models.interfaces.EmployeeFullNameAndOfficiaryId;
import com.officemaneger.areas.employee.models.interfaces.EmployeeFullShortNameView;
import com.officemaneger.areas.employee.models.viewModels.*;
import com.officemaneger.areas.employee.repositories.EmployeeRepository;
import com.officemaneger.areas.phoneContact.entities.PhoneContact;
import com.officemaneger.areas.phoneContact.models.bindingModels.PhoneContactUpdateModel;
import com.officemaneger.areas.phoneContact.models.bindingModels.RegisterPhoneContact;
import com.officemaneger.areas.phoneContact.models.viewModels.PhoneContactViewModel;
import com.officemaneger.areas.phoneContact.services.PhoneContactService;
import com.officemaneger.areas.rank.entities.Rank;
import com.officemaneger.areas.rank.repositories.RankRepository;
import com.officemaneger.areas.user.entities.User;
import com.officemaneger.utility.Constants;
import com.officemaneger.utility.EGN.EGNManager;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;

    private ModelMapper modelMapper;

    private PhoneContactService phoneContactService;

    private RankRepository rankRepository;

    private AnnualLeaveService annualLeaveService;

    private BusinessUnitService businessUnitService;

    public EmployeeServiceImpl() {
    }

    @Override
    public List<EmployeeFullNameViewModel> getNoPositionEmployees() {
        List<EmployeeFullNameViewModel> result = new ArrayList<>();
        List<Employee> employees = this.employeeRepository.findAllByPositionIsNullAndIsActive(true);
        for (Employee employee : employees) {
            List<Employee> employeesRepNames = this.employeeRepository.findAllByFullName(employee.getFullName());
            EmployeeFullNameViewModel employeeFullNameViewModel = this.modelMapper.map(employee, EmployeeFullNameViewModel.class);
            if (employeesRepNames.size() > 1) {
                employeeFullNameViewModel.setFullName(employee.getFullName() + "-" + employee.getOfficiaryID());
            }
            result.add(employeeFullNameViewModel);
        }

        return result;
    }

    @Override
    public List<EmployeeFullNameViewModel> getAllActiveEmployeeFullNameViewModel() {
        List<EmployeeFullNameViewModel> result = new ArrayList<>();
        List<Employee> employees = this.employeeRepository.findAllByIsActive(true);
        for (Employee employee : employees) {
            EmployeeFullNameViewModel employeeFullNameViewModel = this.modelMapper.map(employee, EmployeeFullNameViewModel.class);
            result.add(employeeFullNameViewModel);
        }

        return result;
    }

    @Override
    public List<EmployeeFullNameAndOfficiaryIdViewModel> getAllActiveEmployeeFullNameAndOfficiaryIdViewModel() {
        List<EmployeeFullNameAndOfficiaryIdViewModel> result = new ArrayList<>();
        List<EmployeeFullNameAndOfficiaryId> employees = this.employeeRepository.findAllEmployeeFullNameAndOfficiaryIdByIsActive(true);
        for (EmployeeFullNameAndOfficiaryId employee : employees) {
            EmployeeFullNameAndOfficiaryIdViewModel viewModel = new EmployeeFullNameAndOfficiaryIdViewModel();
            viewModel.setId(employee.getId());
            String fullNameAndOfficiaryId = employee.getFullName() + " - " + employee.getOfficiaryId();
            viewModel.setFullNameAndOfficiaryId(fullNameAndOfficiaryId);

            result.add(viewModel);
        }

        return result;
    }

    @Override
    public void create(CreateEmployeeModel createEmployeeModel) {
        Employee employee = new Employee();
        employee.setEGN(createEmployeeModel.getEgn());
        employee.setFirstName(createEmployeeModel.getFirstName());
        employee.setMiddleName(createEmployeeModel.getMiddleName());
        employee.setLastName(createEmployeeModel.getLastName());
        employee.setAddress(createEmployeeModel.getAddress());
        employee.setBirthDate(EGNManager.getDateOfBirth(createEmployeeModel.getEgn()));
        employee.setOfficiaryID(createEmployeeModel.getOfficiaryID());
        employee.setGender(EGNManager.getGender(createEmployeeModel.getEgn()));
        Rank rank = this.rankRepository.findOne(createEmployeeModel.getRankId());
        employee.setRank(rank);
        employee.setIsActive(true);
        List<RegisterPhoneContact> contacts = createEmployeeModel.getPhoneNumbers();
        for (RegisterPhoneContact contact : contacts) {
            PhoneContact phoneContact = this.phoneContactService.create(contact);
            employee.addPhoneNumber(phoneContact);
            phoneContact.setOwner(employee);
        }
        this.setEmployeeFullShortName(employee);

        this.employeeRepository.save(employee);
    }

    @Override
    public void updateEmployee(EmployeeUpdateModel employeeUpdateModel) {
        Employee employee = this.employeeRepository.findOne(employeeUpdateModel.getId());
        if (employee == null) {
            return;
        }

        employee.setEGN(employeeUpdateModel.getEgn());
        employee.setFirstName(employeeUpdateModel.getFirstName());
        employee.setMiddleName(employeeUpdateModel.getMiddleName());
        employee.setLastName(employeeUpdateModel.getLastName());
        employee.setAddress(employeeUpdateModel.getAddress());
        employee.setBirthDate(EGNManager.getDateOfBirth(employeeUpdateModel.getEgn()));
        employee.setOfficiaryID(employeeUpdateModel.getOfficiaryID());
        employee.setGender(EGNManager.getGender(employeeUpdateModel.getEgn()));
        Rank rank = this.rankRepository.findOne(employeeUpdateModel.getRankId());
        employee.setRank(rank);
        employee.setIsActive(employeeUpdateModel.getIsActive());
        if (employee.getAccounts() != null) {
            for (User user : employee.getAccounts()) {
                user.setEnabled(employeeUpdateModel.getIsActive());
            }
        }
        List<PhoneContactUpdateModel> contacts = employeeUpdateModel.getPhoneNumbers();

        List<PhoneContact> oldPhoneNumbers = employee.getPhoneNumbers();
        List<PhoneContact> phoneNumbersToDelete = new ArrayList<>();
        for (PhoneContact oldPhoneNumber : oldPhoneNumbers) {
            Long oldPhoneNumberId = oldPhoneNumber.getId();
            boolean deleteOldNumber = true;
            for (PhoneContactUpdateModel contact : contacts) {
                if (oldPhoneNumberId.equals(contact.getId())){
                    deleteOldNumber = false;
                    break;
                }
            }
            if (deleteOldNumber) {
                phoneNumbersToDelete.add(oldPhoneNumber);
            }
        }
        for (PhoneContact phoneContact : phoneNumbersToDelete) {
            this.phoneContactService.deletePhoneNumber(phoneContact.getId());
        }
        employee.setPhoneNumbers(new ArrayList<>());
        //check is new or update phone number
        for (PhoneContactUpdateModel contact : contacts) {
            this.phoneContactService.createOrUpdate(contact, employee);
        }
        this.setEmployeeFullShortName(employee);
    }

    @Override
    public List<EmployeeAnnualLeavesViewModel> getActiveEmployeeAnnualLeavesViewModels(List<String> activeAnnualLeaveTypes) {
        List<Employee> activeEmployees = this.employeeRepository.findAllByIsActive(true);
        List<EmployeeAnnualLeavesViewModel> viewModels = new ArrayList<>();
        for (Employee activeEmployee : activeEmployees) {
            EmployeeAnnualLeavesViewModel viewModel = new EmployeeAnnualLeavesViewModel();
            viewModel.setId(activeEmployee.getId());
            viewModel.setFullName(activeEmployee.getFullName());
            List<AnnualLeaveEmployeeViewModel> annualLeaves = new ArrayList<>();
            for (String annualLeaveType : activeAnnualLeaveTypes) {
                boolean hasThisAnnualLeave = false;
                for (AnnualLeave currentAnnualLeave : activeEmployee.getAnnualLeaves()) {
                    if (currentAnnualLeave.getType().getFullName().equals(annualLeaveType)) {
                        annualLeaves.add(this.modelMapper.map(currentAnnualLeave, AnnualLeaveEmployeeViewModel.class));
                        hasThisAnnualLeave = true;
                        break;
                    }
                }
                if (!hasThisAnnualLeave) {
                    annualLeaves.add(this.annualLeaveService.getNewAnnualLeave(activeEmployee, annualLeaveType));
                }
            }
            viewModel.setAnnualLeaves(annualLeaves);

            viewModels.add(viewModel);
        }

        return viewModels;
    }

    @Override
    public List<EmployeeFullShortNameViewModel> getEmployeeFullShortNameViewModel() {
        List<Employee> employees = this.employeeRepository.findAllByIsActive(true);
        List<EmployeeFullShortNameViewModel> employeeFullShortNameViewModels = new ArrayList<>();
        for (Employee employee : employees) {
            EmployeeFullShortNameViewModel newEmployee = new EmployeeFullShortNameViewModel();
            newEmployee.setName(employee.getFullShortName());
            newEmployee.setId(employee.getId());
            employeeFullShortNameViewModels.add(newEmployee);
        }

        return employeeFullShortNameViewModels;
    }

    @Override
    public List<EmployeeFullShortNameViewModel> getEmployeeFullShortNameViewModels(List<Long> employeesIds) {
        List<EmployeeFullShortNameViewModel> viewModels = new ArrayList<>();
        List<EmployeeFullShortNameView> viewInterfacesFromDB = this.employeeRepository.getEmployeeFullShortNameViewInterfaces(employeesIds);
        for (EmployeeFullShortNameView employeeFullShortNameView : viewInterfacesFromDB) {
            viewModels.add(this.modelMapper.map(employeeFullShortNameView, EmployeeFullShortNameViewModel.class));
        }

        return viewModels;
    }

    @Override
    public int getNumbOfActiveEmployees() {
        return this.employeeRepository.getNumbOfEmployees(true);
    }

    @Override
    public int getNumbOfActiveEmployeesInBusinessUnit(Long businessUnitId) {
        return this.employeeRepository.getNumbEmployeesByIsActiveAndBusinessUnit(true, businessUnitId);
    }

    @Override
    public int getNumbOfActiveEmployeesInBusinessUnitAndSubUnit(Long businessUnitId) {
        return this.recursivelyGetNumbOfActiveEmployeesInBusinessUnitAndSubUnit(businessUnitId, 0);
    }

    @Override
    public List<EmployeeFullAdminView> adminGetAllEmployees(boolean isOnlyActiveEmployee) {
        List<Employee> employees = this.employeeRepository.findAllByIsActive(isOnlyActiveEmployee);
        List<EmployeeFullAdminView> employeeFullAdminViews = new ArrayList<>();

        for (Employee employee : employees) {
            EmployeeFullAdminView employeeFullAdminView = this.modelMapper.map(employee, EmployeeFullAdminView.class);
            //TODO
            //TODO
            //TODO
            employeeFullAdminViews.add(employeeFullAdminView);
        }

        return employeeFullAdminViews;
    }

    @Override
    public EmployeeFullNameViewModel getEmployeeFullNameViewModel(Long id) {
        Employee employee = this.employeeRepository.findOne(id);
        if (employee == null) {
            return null;
        }

        List<Employee> employeesRepNames = this.employeeRepository.findAllByFullName(employee.getFullName());
        EmployeeFullNameViewModel employeeFullNameViewModel = this.modelMapper.map(employee, EmployeeFullNameViewModel.class);
        if (employeesRepNames.size() > 1) {
            employeeFullNameViewModel.setFullName(employee.getFullName() + "-" + employee.getOfficiaryID());
        }

        return employeeFullNameViewModel;
    }

    @Override
    public boolean doWeHaveEmployeeWithOfficiaryID(String officiaryID) {
        String officiaryIDFromDB = this.employeeRepository.getOfficiaryIDByOfficiaryID(officiaryID);
        if (officiaryIDFromDB == null) {
            return false;
        }

        return true;
    }

    @Override
    public List<EmployeeViewAllModel> getEmployeeViewAllModels() {
        List<Employee> allEmployees = this.employeeRepository.findAll();
        List<EmployeeViewAllModel> viewAllModels = new ArrayList<>();
        for (Employee employee : allEmployees) {
            EmployeeViewAllModel viewModel = new EmployeeViewAllModel();
            viewModel.setId(employee.getId());
            viewModel.setFullName(employee.getFullName());
            viewModel.setOfficiaryID(employee.getOfficiaryID());
            viewModel.setIsActive(employee.getIsActive());
            String rankName = "";
            if (employee.getRank() != null) {
                rankName = employee.getRank().getName();
            }
            viewModel.setRankName(rankName);
            String businessUnitName = "";
            if (employee.getPosition() != null) {
                businessUnitName = employee.getPosition().getBusinessUnit().getUnitName();
            }
            viewModel.setBusinessUnitName(businessUnitName);

            viewAllModels.add(viewModel);
        }

        return viewAllModels;
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = this.employeeRepository.findOne(employeeId);
        if (employee != null) {
            if (employee.getPosition() != null) {
                employee.getPosition().setEmployee(null);
                employee.setPosition(null);
            }

            this.employeeRepository.delete(employeeId);
        }
    }

    @Override
    public EmployeeEditModel getEmployeeEditModel(Long employeeId) {
        Employee employee = this.employeeRepository.findOne(employeeId);
        if (employee == null) {
            return null;
        }

        EmployeeEditModel viewModel = new EmployeeEditModel();
        viewModel.setId(employeeId);
        viewModel.setFirstName(employee.getFirstName());
        String middleName = "";
        if (employee.getMiddleName() != null) {
            middleName = employee.getMiddleName();
        }
        viewModel.setMiddleName(middleName);
        viewModel.setLastName(employee.getLastName());
        viewModel.setEgn(employee.getEGN());
        viewModel.setOfficiaryID(employee.getOfficiaryID());
        String address = "";
        if (employee.getAddress() != null) {
            address = employee.getAddress();
        }
        viewModel.setAddress(address);
        Long rang = 0L;
        if (employee.getRank() != null) {
            rang = employee.getRank().getId();
        }
        viewModel.setRankId(rang);
        List<PhoneContactViewModel> phoneNumbers = new ArrayList<>();
        for (PhoneContact phoneContact : employee.getPhoneNumbers()) {
            PhoneContactViewModel phoneContactViewModel = new PhoneContactViewModel();
            phoneContactViewModel.setId(phoneContact.getId());
            phoneContactViewModel.setPhoneNumber(phoneContact.getPhoneNumber());
            String phoneType = "";
            if (phoneContact.getPhoneType() != null) {
                phoneType = phoneContact.getPhoneType().getLongBG();
            }
            phoneContactViewModel.setPhoneType(phoneType);

            String description = "";
            if (phoneContact.getDescription() != null) {
                description = phoneContact.getDescription();
            }
            phoneContactViewModel.setDescription(description);

            phoneNumbers.add(phoneContactViewModel);
        }
        viewModel.setPhoneNumbers(phoneNumbers);
        viewModel.setIsActive(employee.getIsActive());

        return viewModel;
    }

    @Override
    public boolean doWeHaveEmployeeWithOfficiaryIDWhenUpdate(Long employeeId, String officiaryID) {
        Long employeeIdFromDB = this.employeeRepository.getIdByOfficiaryID(officiaryID);
        if (employeeIdFromDB == null || employeeIdFromDB.equals(employeeId)) {
            return false;
        }

        return true;
    }

    @Override
    public List<Long> getActiveEmployeesIds() {
        return this.employeeRepository.getEmployeesIdsByIsActive(true);
    }

    @Override
    public EmployeeWorkShiftViewModel getEmployeeWorkShiftViewModel(Long employeeId) {
        String fullName = this.employeeRepository.getFullNameByEmployeeId(employeeId);
        if (fullName == null) {
            return null;
        }

        EmployeeWorkShiftViewModel viewModel = new EmployeeWorkShiftViewModel();
        viewModel.setId(employeeId);
        viewModel.setFullName(fullName);
        List<AnnualLeaveWorkShiftViewModel> annualLeaves = this.annualLeaveService.getAnnualLeaveWorkShiftViewModel(employeeId);
        viewModel.setAnnualLeaves(annualLeaves);
        StringBuilder sb = new StringBuilder();
        sb.append(fullName).append(Constants.HTML_ELEMENT_NEW_LINE);
        for (int i = 0; i < annualLeaves.size(); i++) {
            AnnualLeaveWorkShiftViewModel annualLeave = annualLeaves.get(i);
            sb.append(annualLeave.getAnnualLeaveType());
            sb.append(" - ");
            sb.append(annualLeave.getNumberOfAnnualLeave());
            sb.append("дни");
            if (i != annualLeaves.size() - 1) {
                sb.append(Constants.HTML_ELEMENT_NEW_LINE);
            }
        }
        if (annualLeaves.size() == 0) {
            sb.append("Няма отпуска!");
        }
        viewModel.setTooltipText(sb.toString());

        return viewModel;
    }

    @Override
    public EmployeeFullNameAndOfficiaryIdViewModel getEmployeeFullNameAndOfficiaryIdViewModel(Long employeeId) {
        EmployeeFullNameAndOfficiaryId employee = this.employeeRepository.getEmployeeFullNameAndOfficiaryIdByEmployeeId(employeeId);
        if (employee == null) {
            return null;
        }

        EmployeeFullNameAndOfficiaryIdViewModel viewModel = new EmployeeFullNameAndOfficiaryIdViewModel();
        viewModel.setId(employee.getId());
        String fullNameAndOfficiaryId = employee.getFullName();
        fullNameAndOfficiaryId = fullNameAndOfficiaryId + " - " + employee.getOfficiaryId();
        viewModel.setFullNameAndOfficiaryId(fullNameAndOfficiaryId);

        return viewModel;
    }

    @Override
    public Long getBusinessUnitIdByEmployeeId(Long employeeId) {
        return this.employeeRepository.getBusinessUnitIdByEmployeeId(employeeId);
    }

    @Override
    public boolean isEmployeePermanentBoss(Long employeeId) {
        Long employeeBusinessUnitId = this.employeeRepository.getBusinessUnitIdByEmployeeId(employeeId);
        Long bossIdByBusinessUnitId = this.businessUnitService.getPermanentBossIdByBusinessUnit(employeeBusinessUnitId);

        return employeeBusinessUnitId.equals(bossIdByBusinessUnitId);
    }

    @Autowired
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Autowired
    public void setPhoneContactService(PhoneContactService phoneContactService) {
        this.phoneContactService = phoneContactService;
    }

    @Autowired
    public void setBusinessUnitService(BusinessUnitService businessUnitService) {
        this.businessUnitService = businessUnitService;
    }

    @Autowired
    public void setRankRepository(RankRepository rankRepository) {
        this.rankRepository = rankRepository;
    }

    @Autowired
    public void setAnnualLeaveService(AnnualLeaveService annualLeaveService) {
        this.annualLeaveService = annualLeaveService;
    }

    private void setEmployeeFullShortName(Employee employee) {
        String middleName = "";
        if (employee.getMiddleName() != null && !employee.getMiddleName().isEmpty()) {
            String middleNameFirstLetter =  employee.getMiddleName().substring(0,1).toUpperCase();
            String testFullShortName = employee.getFirstName() + " " + middleNameFirstLetter + ". " + employee.getLastName();
            Employee databaseEmployee = this.employeeRepository.findOneByFullShortName(testFullShortName);
            if(databaseEmployee != null) {
                boolean isFoundUniqueName = false;
                for (int i = 2; i < employee.getMiddleName().length() - 1; i++) {
                    testFullShortName = employee.getFirstName() + " " + middleNameFirstLetter +
                            employee.getMiddleName().substring(1, i) + ". " + employee.getLastName();
                    databaseEmployee = this.employeeRepository.findOneByFullShortName(testFullShortName);
                    if(databaseEmployee == null) {
                        middleName =  " " + middleNameFirstLetter + employee.getMiddleName().substring(1, i) + ". ";
                        isFoundUniqueName = true;
                        break;
                    }
                }

                if (!isFoundUniqueName) {
                    for (int i = 1; i < 20; i++) {
                        String str = "_";
                        String repeatedExtraLetters = StringUtils.repeat(str, i);
                        testFullShortName = employee.getFirstName() + " " + middleNameFirstLetter + ". " + employee.getLastName() + repeatedExtraLetters;
                        databaseEmployee = this.employeeRepository.findOneByFullShortName(testFullShortName);
                        if(databaseEmployee == null) {
                            employee.setFullShortName(testFullShortName);
                            return;
                        }
                    }
                }
            } else {
                employee.setFullShortName(testFullShortName);
                return;
            }

        } else {
            for (int i = 1; i < 20; i++) {
                String str = "_";
                String repeatedExtraLetters = StringUtils.repeat(str, i);
                String testFullShortName = employee.getFirstName() + " " + employee.getLastName() + repeatedExtraLetters;
                Employee databaseEmployee = this.employeeRepository.findOneByFullShortName(testFullShortName);
                if(databaseEmployee == null) {
                    employee.setFullShortName(testFullShortName);
                    return;
                }
            }
        }

        String fullShortName = employee.getFirstName() + middleName + employee.getLastName();
        employee.setFullShortName(fullShortName);
    }

    private Integer recursivelyGetNumbOfActiveEmployeesInBusinessUnitAndSubUnit(Long businessUnitId, Integer result) {
        result += this.employeeRepository.getNumbEmployeesByIsActiveAndBusinessUnit(true, businessUnitId);
        List<Long> subUnitIds = this.businessUnitService.getSubUnitsIds(businessUnitId);
        for (Long subUnitId : subUnitIds) {
            result = this.recursivelyGetNumbOfActiveEmployeesInBusinessUnitAndSubUnit(subUnitId, result);
        }

        return result;
    }
}
