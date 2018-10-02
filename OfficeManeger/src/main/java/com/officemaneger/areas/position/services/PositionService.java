package com.officemaneger.areas.position.services;

import com.officemaneger.areas.position.models.bindingModels.PositionAddNewModel;
import com.officemaneger.areas.position.models.bindingModels.PositionCreationModel;
import com.officemaneger.areas.position.models.viewModels.PositionNameViewModel;
import com.officemaneger.areas.position.models.viewModels.PositionViewInEditBusinessUnitModel;

import java.util.List;

public interface PositionService {

    Long create(PositionCreationModel positionCreationModel);

    List<PositionNameViewModel> getAllPositionNamesViewModels();

    PositionNameViewModel getPositionNameViewModel(Long id);

    List<String> getAllPositionNames();

    boolean isNameOccupied(String name);

    PositionViewInEditBusinessUnitModel getPositionViewInEditBusinessUnitModel(Long id);

    void delete(long positionId);

    PositionNameViewModel addNewPosition(PositionAddNewModel positionAddNewModel);

    void updatePositionName(String newPositionName, Long positionId);

    void updatePositionEmployee(Long positionId, Long newEmployeeId);
}
