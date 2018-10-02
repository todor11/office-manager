package com.officemaneger.areas.annualLeave.repositories;

import com.officemaneger.areas.annualLeave.entities.AnnualLeave;
import com.officemaneger.areas.annualLeave.models.interfaces.AnnualLeaveEmployeeView;
import com.officemaneger.areas.annualLeave.models.interfaces.AnnualLeaveWorkShiftView;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.employee.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnualLeaveRepository extends JpaRepository<AnnualLeave, Long> {

    AnnualLeave findOneByTypeAndOwner(AnnualLeaveType type, Employee owner);

    @Query(value = "SELECT a.id FROM AnnualLeave AS a " +
            "WHERE a.owner.id = :employeeId " +
            "AND a.type.id = :annualLeaveTypeId")
    Long getAnnualLeaveIdByEmployeeIdAndTypeId(@Param("employeeId") Long employeeId, @Param("annualLeaveTypeId") Long annualLeaveTypeId);

    @Query(value = "SELECT COUNT(a.id) " +
            "FROM AnnualLeave AS a " +
            "WHERE a.type.id = :annualLeaveTypeId")
    Long countByAnnualLeaveType(@Param("annualLeaveTypeId") Long annualLeaveTypeId);

    @Query(value = "SELECT a.id AS id, " +
            "a.numberOfAnnualLeave AS numberOfAnnualLeave, " +
            "a.type.fullName AS annualLeaveType, " +
            "a.type.id AS annualLeaveTypeId, " +
            "a.type.shortName AS annualLeaveTypeShort " +
            "FROM AnnualLeave AS a " +
            "WHERE a.owner.id = :employeeId " +
            "AND a.numberOfAnnualLeave > 0 " +
            "ORDER BY a.type.fullName")
    List<AnnualLeaveWorkShiftView> getAnnualLeaveWorkShiftViewByEmployeeIdOrdered(@Param("employeeId") Long employeeId);

    @Query(value = "SELECT a.numberOfAnnualLeave FROM AnnualLeave AS a " +
            "WHERE a.owner.id = :employeeId " +
            "AND a.type.id = :annualLeaveTypeId")
    int getNumbOfAnnualLeaveByEmployeeIdAndAnnualLeaveTypeId(@Param("employeeId") Long employeeId, @Param("annualLeaveTypeId") Long annualLeaveTypeId);

    @Modifying
    @Query("UPDATE AnnualLeave a " +
            "SET a.numberOfAnnualLeave = :numberOfAnnualLeave " +
            "WHERE a.id = :annualLeaveId")
    void setNumberOfAnnualLeave(@Param("annualLeaveId") Long annualLeaveId,@Param("numberOfAnnualLeave") int numberOfAnnualLeave);

    @Query(value = "SELECT a " +
            "FROM AnnualLeave AS a " +
            "WHERE a.owner.id = :employeeId " +
            "AND a.numberOfAnnualLeave > 0")
    List<AnnualLeave> getAnnualLeavesByEmployeeId(@Param("employeeId") Long employeeId);

    //@Query(value = "SELECT a.id AS id," +
    //        "a.numberOfAnnualLeave AS numberOfAnnualLeave " +
    //        "FROM AnnualLeave AS a " +
    //        "WHERE a.owner.id = :employeeId " +
    //        "AND a.type.fullName = :annualLeaveTypeFullName")
    //AnnualLeaveEmployeeView getAnnualLeaveEmployeeViewByEmployeeIdAndTypeFullName(@Param("employeeId") Long employeeId, @Param("annualLeaveTypeFullName") String annualLeaveTypeFullName);
}
