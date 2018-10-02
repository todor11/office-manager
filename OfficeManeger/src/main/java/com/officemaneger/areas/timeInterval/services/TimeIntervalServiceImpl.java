package com.officemaneger.areas.timeInterval.services;

import com.officemaneger.areas.timeInterval.repositories.TimeIntervalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TimeIntervalServiceImpl implements TimeIntervalService {

    private TimeIntervalRepository timeIntervalRepository;

    public TimeIntervalServiceImpl() {
    }

    @Autowired
    public void setTimeIntervalRepository(TimeIntervalRepository timeIntervalRepository) {
        this.timeIntervalRepository = timeIntervalRepository;
    }
}
