package com.officemaneger.areas.workDay.repositories;

import com.officemaneger.areas.workDay.entities.WorkDay;
import com.officemaneger.enums.WorkingDayType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {

    List<WorkDay> findAllByWorkingDayTypeAndIsRestDayOfWeek(WorkingDayType workingDayType, boolean isRestDayOfWeek);

    List<WorkDay> findAllByWorkingDayType(WorkingDayType workingDayType);

    @Query("SELECT w FROM WorkDay AS w " +
            "WHERE w.date = :date ")
    WorkDay getWorkDayByDate(@Param("date") LocalDate date);

    @Query("SELECT w.date FROM WorkDay AS w " +
            "WHERE w.date >= :startDate " +
            "AND w.date <= :endDate " +
            "AND w.workingDayType = :workingDayType ")
    List<LocalDate> getDatesFromDateToDateByType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("workingDayType") WorkingDayType workingDayType);

    @Query(value = "SELECT COUNT(w) FROM WorkDay AS w " +
            "WHERE w.date >= :startDate " +
            "AND w.date <= :endDate ")
    Long getNumbOfWorkDaysBetweenDates(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query(value = "SELECT w.id FROM WorkDay AS w " +
            "WHERE w.date = :date ")
    Long getWorkDayIdByDate(@Param("date") LocalDate date);

    @Query(value = "SELECT COUNT(w) FROM WorkDay AS w " +
            "WHERE w.date >= :startDate " +
            "AND w.date <= :endDate " +
            "AND w.workingDayType = :workingDayType ")
    Long getNumbOfWorkDaysBetweenDatesByType(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate, @Param("workingDayType") WorkingDayType workingDayType);

}
