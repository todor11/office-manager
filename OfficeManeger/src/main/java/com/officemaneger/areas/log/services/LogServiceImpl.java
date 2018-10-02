package com.officemaneger.areas.log.services;

import com.officemaneger.areas.log.entities.Log;
import com.officemaneger.areas.log.models.dtoModels.LogDto;
import com.officemaneger.areas.log.repositories.LogRepository;
import com.officemaneger.areas.log.services.LogService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LogServiceImpl implements LogService {

    private LogRepository logRepository;

    public LogServiceImpl() {
    }

    @Override
    public void create(LogDto logDto) {
        ModelMapper modelMapper = new ModelMapper();

        Log entity = new Log();
        modelMapper.map(logDto, entity);

        this.logRepository.save(entity);
    }

    @Autowired
    public void setLogRepository(LogRepository logRepository) {
        this.logRepository = logRepository;
    }
}
