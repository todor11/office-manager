package com.officemaneger.areas.shiftTypeGroup.repositories;

import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;
import com.officemaneger.areas.shiftTypeGroup.models.interfaces.ShiftTypeGroupWorkScheduleSettingsView;
import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupWorkScheduleSettingsViewModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftTypeGroupRepository extends JpaRepository<ShiftTypeGroup, Long> {

    @Query(value = "SELECT g.id AS id, " +
            "g.groupOrderWeight AS groupOrderWeight, " +
            "g.groupName AS groupName " +
            "FROM ShiftTypeGroup AS g " +
            "ORDER BY g.groupOrderWeight")
    List<ShiftTypeGroupWorkScheduleSettingsView> getAllShiftTypeGroupWorkScheduleSettingsViewModels();
}
