package com.officemaneger.areas.employee.repositories;

import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.employee.models.interfaces.EmployeeFullNameAndOfficiaryId;
import com.officemaneger.areas.employee.models.interfaces.EmployeeFullShortNameView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee,Long> {

    List<Employee> findAllByPositionIsNullAndIsActive(boolean isActive);

    List<Employee> findAllByFullName(String fullName);

    Employee findOneByFullShortName(String fullShortName);

    List<Employee> findAllByIsActive(boolean isActive);

    @Query("SELECT e.officiaryID FROM Employee AS e " +
            "WHERE e.officiaryID = :officiaryID ")
    String getOfficiaryIDByOfficiaryID(@Param("officiaryID") String officiaryID);

    @Query("SELECT e.id FROM Employee AS e " +
            "WHERE e.officiaryID = :officiaryID ")
    Long getIdByOfficiaryID(@Param("officiaryID") String officiaryID);

    @Query("SELECT e.id FROM Employee AS e " +
            "JOIN e.rank AS r " +
            "WHERE e.isActive = :isActive " +
            "ORDER BY r.rankRate DESC")
    List<Long> getEmployeesIdsByIsActive(@Param("isActive") boolean isActive);

    @Query("SELECT e.fullName FROM Employee AS e " +
            "WHERE e.id = :employeeId ")
    String getFullNameByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT bu.id FROM Employee AS e " +
            "JOIN e.position AS p " +
            "JOIN p.businessUnit AS bu " +
            "WHERE e.id = :employeeId ")
    Long getBusinessUnitIdByEmployeeId(@Param("employeeId")Long employeeId);

    @Query("SELECT e.id AS id, " +
            "e.fullName AS fullName, " +
            "e.officiaryID AS officiaryId " +
            "FROM Employee AS e " +
            "WHERE e.isActive = :isActive")
    List<EmployeeFullNameAndOfficiaryId> findAllEmployeeFullNameAndOfficiaryIdByIsActive(@Param("isActive") boolean isActive);

    @Query("SELECT e.id AS id, " +
            "e.fullName AS fullName, " +
            "e.officiaryID AS officiaryId " +
            "FROM Employee AS e " +
            "WHERE e.id = :employeeId")
    EmployeeFullNameAndOfficiaryId getEmployeeFullNameAndOfficiaryIdByEmployeeId(@Param("employeeId") Long employeeId);

    @Query("SELECT e.id AS id, " +
            "e.fullShortName AS name " +
            "FROM Employee AS e " +
            "WHERE e.id IN :employeesIds")
    List<EmployeeFullShortNameView> getEmployeeFullShortNameViewInterfaces(@Param("employeesIds") List<Long> employeesIds);

    @Query("SELECT COUNT(e) " +
            "FROM Employee AS e " +
            "WHERE e.isActive = :isActive")
    int getNumbOfEmployees(@Param("isActive") boolean isActive);

    @Query("SELECT COUNT(e) " +
            "FROM Employee AS e " +
            "JOIN e.position AS p " +
            "JOIN p.businessUnit AS bu " +
            "WHERE e.isActive = :isActive " +
            "AND bu.id = :businessUnitId")
    int getNumbEmployeesByIsActiveAndBusinessUnit(@Param("isActive") boolean isActive,@Param("businessUnitId") Long businessUnitId);
}
