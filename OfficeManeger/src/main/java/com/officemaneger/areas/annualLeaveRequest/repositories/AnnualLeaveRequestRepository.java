package com.officemaneger.areas.annualLeaveRequest.repositories;

import com.officemaneger.areas.annualLeaveRequest.entities.AnnualLeaveRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnualLeaveRequestRepository extends JpaRepository<AnnualLeaveRequest, Long> {

    @Query("SELECT r " +
            "FROM AnnualLeaveRequest AS r " +
            "JOIN r.employee AS e " +
            "JOIN e.position AS p " +
            "JOIN p.businessUnit AS bu " +
            "WHERE e IS NOT NULL " +
            "AND p IS NOT NULL " +
            "AND bu IS NOT NULL " +
            "AND bu.id = :businessUnitId " +
            "AND r.isApproved = :isApproved ")
    List<AnnualLeaveRequest> getAllByBusinessUnit(@Param("businessUnitId") Long businessUnitId, @Param("isApproved") boolean isApproved);

    @Query("SELECT r " +
            "FROM AnnualLeaveRequest AS r " +
            "JOIN r.employee AS e " +
            "WHERE e IS NOT NULL " +
            "AND e.id = :employeeId " +
            "AND r.isApproved = :isApproved ")
    List<AnnualLeaveRequest> getRequestByEmployee(@Param("employeeId") Long employeeId, @Param("isApproved") boolean isApproved);

    @Query("SELECT e.id " +
            "FROM AnnualLeaveRequest AS r " +
            "JOIN r.employee AS e " +
            "WHERE r.id = :requestId " +
            "AND e IS NOT NULL ")
    Long getEmployeeIdByRequestId(@Param("requestId") Long requestId);

}
