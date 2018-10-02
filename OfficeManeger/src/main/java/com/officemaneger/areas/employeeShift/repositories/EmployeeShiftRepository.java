package com.officemaneger.areas.employeeShift.repositories;

import com.officemaneger.areas.employeeShift.entities.EmployeeShift;
import com.officemaneger.areas.employeeShift.models.interfaces.EmployeeFullShortNameShiftView;
import com.officemaneger.areas.employeeShift.models.interfaces.EmployeeShiftTypeAndDate;
import com.officemaneger.areas.employeeShift.models.interfaces.EmployeeShiftViewAll;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmployeeShiftRepository extends JpaRepository<EmployeeShift, Long> {

    @Query(value = "SELECT esh.id AS id, " +
            "t AS dualNameType," +
            "esh.shiftDuration AS shiftDuration, " +
            "esh.breakDuration AS breakDuration, " +
            "esh.startTime AS startTime, " +
            "esh.endTime AS endTime, " +
            "esh.secondStartTime AS secondStartTime, " +
            "esh.secondEndTime AS secondEndTime, " +
            "esh.isEndOnNextDay AS isEndOnNextDay " +
            "FROM EmployeeShift AS esh " +
            "JOIN esh.owner AS ow " +
            "JOIN esh.type AS t " +
            "WHERE ow.id = :employeeId " +
            "AND esh.date = :date")
    EmployeeShiftViewAll getEmployeeShiftViewAllByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    @Query(value = "SELECT esh " +
            "FROM EmployeeShift AS esh " +
            "WHERE esh.owner.id = :employeeId " +
            "AND esh.date = :date")
    EmployeeShift getEmployeeByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    @Query(value = "SELECT esh.id " +
            "FROM EmployeeShift AS esh " +
            "WHERE esh.owner.id = :employeeId " +
            "AND esh.date = :date")
    Long getEmployeeShiftIdByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    @Query(value = "SELECT esh.type.id " +
            "FROM EmployeeShift AS esh " +
            "WHERE esh.owner.id = :employeeId " +
            "AND esh.date = :date")
    Long getEmployeeShiftTypeIdByEmployeeIdAndDate(@Param("employeeId") Long employeeId, @Param("date") LocalDate date);

    @Query(value = "SELECT esh.owner.id " +
            "FROM EmployeeShift AS esh " +
            "WHERE esh.id = :employeeShiftId")
    Long getEmployeeIdByEmployeeShiftId(@Param("employeeShiftId") Long employeeShiftId);

    @Query(value = "SELECT e.id " +
            "FROM EmployeeShift AS esh " +
            "JOIN esh.owner AS e " +
            "JOIN esh.type AS t " +
            "WHERE esh.date = :date " +
            "AND t IS NOT NULL " +
            "AND e IS NOT NULL " +
            "AND t.id = :shiftTypeId ")
    List<Long> getEmployeeIdsByDateAndShiftType(@Param("date") LocalDate date,@Param("shiftTypeId") Long shiftTypeId);

    @Query(value = "SELECT esh.id AS employeeShiftId, " +
            "e.id AS employeeId, " +
            "e.firstName AS employeeFirstName, " +
            "e.middleName AS employeeMiddleName, " +
            "e.lastName AS employeeLastName " +
            "FROM EmployeeShift AS esh " +
            "JOIN esh.owner AS e " +
            "JOIN esh.type AS t " +
            "JOIN e.position AS p " +
            "JOIN p.businessUnit AS bu " +
            "WHERE esh.date = :date " +
            "AND t IS NOT NULL " +
            "AND e IS NOT NULL " +
            "AND p IS NOT NULL " +
            "AND bu IS NOT NULL " +
            "AND bu.id = :businessUnitId " +
            "AND t.id = :shiftTypeId ")
    List<EmployeeFullShortNameShiftView> getEmployeeInfoByDateAndShiftTypeAndBusinessUnit(@Param("date") LocalDate date,@Param("shiftTypeId") Long shiftTypeId,@Param("businessUnitId") Long businessUnitId);

    @Query(value = "SELECT esh.date " +
            "FROM EmployeeShift AS esh " +
            "JOIN esh.owner AS e " +
            "JOIN esh.type AS t " +
            "WHERE e.id = :employeeId " +
            "AND t IS NOT NULL " +
            "AND e IS NOT NULL " +
            "AND t.id = :shiftTypeId ")
    List<LocalDate> getShiftDatesByEmployeeIdAndTypeId(@Param("employeeId") Long employeeId,@Param("shiftTypeId")  Long shiftTypeId);

    @Query(value = "SELECT t.fullName AS shiftType, " +
            "esh.date AS date " +
            "FROM EmployeeShift AS esh " +
            "JOIN esh.owner AS e " +
            "JOIN esh.type AS t " +
            "WHERE e.id = :employeeId " +
            "AND t IS NOT NULL " +
            "AND e IS NOT NULL " +
            "AND t.id IN :shiftTypeIds " +
            "ORDER BY esh.date DESC")
    List<EmployeeShiftTypeAndDate> getShiftTypeAndDatesByEmployeeIdAndTypeOrderedByDateDesc(@Param("employeeId") Long employeeId, @Param("shiftTypeIds") List<Long> shiftTypeIds);

    @Query(value = "SELECT esh " +
            "FROM EmployeeShift AS esh " +
            "JOIN esh.owner AS e " +
            "WHERE e.id = :employeeId " +
            "AND e IS NOT NULL " +
            "AND esh.date IN :shiftDatesToObserve " +
            "ORDER BY esh.date ASC")
    List<EmployeeShift> getEmployeeShiftsByEmployeeAndDatesOrd(@Param("employeeId") Long employeeId,@Param("shiftDatesToObserve") List<LocalDate> shiftDatesToObserve);
}
