package com.officemaneger.areas.businessUnit.services;

import com.officemaneger.areas.businessUnit.models.bindingModels.BusinessUnitCreationModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitEditViewModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitNameViewModel;
import com.officemaneger.areas.businessUnit.models.viewModels.BusinessUnitViewModel;

import java.util.List;

public interface BusinessUnitService {

    void create(BusinessUnitCreationModel businessUnitCreationModel);

    List<BusinessUnitNameViewModel> getAllBusinessUnitNameViewModels();

    List<BusinessUnitViewModel> getAllBusinessUnitViewModels();

    BusinessUnitNameViewModel getBusinessUnitViewModel(Long id);

    List<String> getAllBusinessUnitNames();

    BusinessUnitNameViewModel findOneByUnitName(String unitName);

    boolean isNameOccupied(String unitName);

    boolean isNameOccupied(String newUnitName, Long unitId);

    void delete(Long id);

    BusinessUnitEditViewModel getBusinessUnitEditViewModel(Long id);

    void updateUnitName(Long unitId, String newUnitName);

    String getBusinessUnitName(Long businessUnitId);

    void updateMainUnit(Long unitId, Long mainUnitId);

    List<Long> getBusinessUnitEmployeesIds(Long businessUnitId);

    Long getMainUnitIdByUnitId(Long businessUnitId);

    Long getPermanentBossIdByBusinessUnit(Long businessUnitId);

    List<Long> getSubUnitsIds(Long businessUnitId);

    List<Long> getBossesIdsByBusinessUnit(Long businessUnitId);

    int getNumbOfPositionsInBusinessUnit(Long businessUnitId);

    int getNumbOfEmployeePositionsInBusinessUnit(Long businessUnitId);
}
