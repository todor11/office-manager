package com.officemaneger.areas.timeInterval.repositories;

import com.officemaneger.areas.timeInterval.entities.TimeInterval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TimeIntervalRepository extends JpaRepository<TimeInterval, Long> {

}
