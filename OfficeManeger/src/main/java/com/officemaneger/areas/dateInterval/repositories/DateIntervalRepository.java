package com.officemaneger.areas.dateInterval.repositories;

import com.officemaneger.areas.dateInterval.entities.DateInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DateIntervalRepository extends JpaRepository<DateInterval, Long> {
}
