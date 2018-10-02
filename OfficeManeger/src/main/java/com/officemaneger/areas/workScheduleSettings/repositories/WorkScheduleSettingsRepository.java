package com.officemaneger.areas.workScheduleSettings.repositories;

import com.officemaneger.areas.workScheduleSettings.entities.WorkScheduleSettings;
import com.officemaneger.areas.workScheduleSettings.models.interfaces.SettingsView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkScheduleSettingsRepository extends JpaRepository<WorkScheduleSettings, Long> {

    @Query("SELECT ws.id " +
            "FROM WorkScheduleSettings AS ws " +
            "JOIN ws.businessUnit AS bu " +
            "WHERE bu.id = :businessUnitId ")
    Long getSettingsIdByBusinessUnitId(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT sht.id " +
            "FROM WorkScheduleSettings AS ws " +
            "JOIN ws.businessUnit AS bu " +
            "JOIN ws.shiftTypeGroupsToObserve AS sht " +
            "WHERE bu.id = :businessUnitId ")
    List<Long> getActiveWorkShiftTypeGroupsIdsByBusinessUnitId(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT ws.numbOfEmployeesInShift " +
            "FROM WorkScheduleSettings AS ws " +
            "JOIN ws.businessUnit AS bu " +
            "WHERE bu.id = :businessUnitId ")
    int getNumbOfEmployeesInShiftByBusinessUnitId(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT ws.numbOfEmployeesInShift " +
            "FROM WorkScheduleSettings AS ws " +
            "WHERE ws.id = :settingsId ")
    int getNumbOfEmployeesInShift(@Param("settingsId") Long settingsId);

    @Query("SELECT st.id " +
            "FROM WorkScheduleSettings AS ws " +
            "JOIN ws.shiftTypeGroupsToObserve AS st " +
            "WHERE ws.id = :settingsId ")
    List<Long> getShiftTypeGroupsToObserveIds(@Param("settingsId") Long settingsId);

    @Query("SELECT bu.id " +
            "FROM WorkScheduleSettings AS ws " +
            "JOIN ws.businessUnit AS bu " +
            "WHERE ws.id = :settingsId " +
            "AND bu IS NOT NULL")
    Long getBusinessUnitIdBySettingsId(@Param("settingsId") Long settingsId);

    @Query(value = "SELECT s.numbOfEmployeesInShift AS numbOfEmployeesInShift, " +
            "s.isOnDoubleShiftRegime AS isOnDoubleShiftRegime, " +
            "s.isOnTripleShiftRegime AS isOnTripleShiftRegime " +
            "FROM WorkScheduleSettings AS s " +
            "WHERE s.id = :settingsId")
    SettingsView getSettingsView(@Param("settingsId") Long settingsId);

    @Query("SELECT ws " +
            "FROM WorkScheduleSettings AS ws " +
            "JOIN ws.businessUnit AS bu " +
            "WHERE bu.id = :businessUnitId ")
    WorkScheduleSettings getSettingsByBusinessUnitId(@Param("businessUnitId") Long businessUnitId);
}
