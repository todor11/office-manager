package com.officemaneger.areas.shiftType.repositories;

import com.officemaneger.areas.shiftType.entities.ShiftType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShiftTypeRepository extends JpaRepository<ShiftType, Long> {

    List<ShiftType> findAllByIsActiveOrderByIsRegularShiftDescShortNameAsc(boolean isActive);

    @Query("SELECT sht " +
            "FROM ShiftType AS sht " +
            "WHERE sht.id IN :shiftTypesIds " +
            "ORDER BY sht.isRegularShift DESC, " +
            "sht.shortName ASC ")
    List<ShiftType> findAllByIdOrderByIsRegularShiftDescShortNameAsc(@Param("shiftTypesIds") List<Long> shiftTypesIds);

    ShiftType findOneByFullName(String FullName);

    ShiftType findOneByShortName(String ShortName);

    List<ShiftType> findAllByIsActiveAndIsRegularShiftOrderByIsRegularShiftDescShortNameAsc(boolean isActive, boolean isRegular);

    List<ShiftType> findAllByIsActiveAndIsRegularShiftOrderByFullNameAsc(boolean isActive, boolean isRegular);
}
