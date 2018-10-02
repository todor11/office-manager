package com.officemaneger.areas.dualNameTypes.services;

import com.officemaneger.areas.dualNameTypes.repositories.DualNameTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DualNameTypeServiceImpl implements DualNameTypeService {

    private DualNameTypeRepository dualNameTypeRepository;

    public DualNameTypeServiceImpl() {
    }

    @Override
    public String getShortNameType(Long dualNameTypeId) {
        return this.dualNameTypeRepository.getShortNameType(dualNameTypeId);
    }

    @Autowired
    public void setDualNameTypeRepository(DualNameTypeRepository dualNameTypeRepository) {
        this.dualNameTypeRepository = dualNameTypeRepository;
    }
}
