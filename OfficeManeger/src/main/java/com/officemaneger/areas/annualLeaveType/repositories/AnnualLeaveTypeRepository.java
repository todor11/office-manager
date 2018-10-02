package com.officemaneger.areas.annualLeaveType.repositories;

import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnualLeaveTypeRepository extends JpaRepository<AnnualLeaveType, Long> {

    List<AnnualLeaveType> findAllByIsActive(boolean isActive);

    @Query(value = "SELECT a.fullName FROM AnnualLeaveType AS a " +
            "WHERE a.isActive = :isActive " +
            "ORDER BY a.fullName")
    List<String> getAnnualLeaveTypeFullNameByIsActiveOrdered(@Param("isActive") boolean isActive);

    AnnualLeaveType findOneByFullName(String FullName);

    AnnualLeaveType findOneByShortName(String ShortName);

    @Query(value = "SELECT a.id FROM AnnualLeaveType AS a " +
            "WHERE a.id = :dualNameTypeId")
    Long getAnnualLeaveTypeIdByDualNameTypeId(@Param("dualNameTypeId") Long dualNameTypeId);

    @Query(value = "SELECT a.id FROM AnnualLeaveType AS a")
    List<Long> getAllAnnualLeaveTypeIds();
}
