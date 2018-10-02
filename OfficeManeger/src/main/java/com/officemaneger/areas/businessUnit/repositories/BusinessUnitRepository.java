package com.officemaneger.areas.businessUnit.repositories;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BusinessUnitRepository extends JpaRepository<BusinessUnit, Long> {

    BusinessUnit findOneByUnitName(String unitName);

    //List<BusinessUnit> findAllByUnitName(String unitName);

    @Query("SELECT bu.unitName FROM BusinessUnit AS bu " +
            "WHERE bu.id = :businessUnitId ")
    String getBusinessUnitName(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT bp.employee.id FROM BusinessUnit AS bu " +
            "JOIN bu.bossPosition AS bp " +
            "WHERE bu.id = :businessUnitId " +
            "AND bp.employee IS NOT NULL ")
    Long getBossIdByBusinessUnitId(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT p.employee.id " +
            "FROM BusinessUnit AS bu " +
            "JOIN bu.positions AS p " +
            "JOIN p.employee AS e " +
            "JOIN e.rank AS r " +
            "WHERE bu.id = :businessUnitId " +
            "AND p.employee IS NOT NULL " +
            "AND p.id != bu.bossPosition.id " +
            "ORDER BY r.rankRate DESC")
    List<Long> getEmployeesIdsByBusinessUnitId(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT bu.id FROM BusinessUnit AS bu " +
            "WHERE bu.mainUnit.id = :businessUnitId ")
    List<Long> getSubUnitIdsByBusinessUnitId(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT bu.mainUnit.id FROM BusinessUnit AS bu " +
            "WHERE bu.id = :businessUnitId " +
            "AND bu.mainUnit IS NOT NULL")
    Long getMainUnitIdByUnitId(@Param("businessUnitId") Long businessUnitId);

    @Query("SELECT COUNT(p) " +
            "FROM BusinessUnit AS bu " +
            "JOIN bu.positions AS p " +
            "JOIN p.employee AS e " +
            "WHERE bu.id = :businessUnitId " +
            "AND e IS NOT NULL")
    int getNumbOfEmployeePositionsInBusinessUnit(@Param("businessUnitId") Long businessUnitId);
}
