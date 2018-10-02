package com.officemaneger.areas.annualLeaveDateInterval.repositories;

import com.officemaneger.areas.annualLeaveDateInterval.entities.AnnualLeaveDateInterval;
import com.officemaneger.areas.annualLeaveDateInterval.models.AnnualLeaveDateIntervalUpdateView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnualLeaveDateIntervalRepository extends JpaRepository<AnnualLeaveDateInterval, Long> {

    @Query(value = "SELECT d.id AS id, " +
            "d.startDate AS startDate, " +
            "d.endDate AS endDate, " +
            "t.id AS shiftTypeId " +
            "FROM AnnualLeaveDateInterval AS d " +
            "JOIN d.type AS t " +
            "JOIN d.annualLeaveRequest AS r " +
            "WHERE r.id = :requestId")
    List<AnnualLeaveDateIntervalUpdateView> getAnnualLeaveDateIntervalUpdateViewModelsByRequestId(@Param("requestId") Long requestId);
}
