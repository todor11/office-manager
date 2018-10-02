package com.officemaneger.areas.person.services;

import com.officemaneger.areas.person.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    private PersonRepository personRepository;

    public PersonServiceImpl() {
    }

    @Override
    public boolean doWeHavePersonWithEGN(String egn) {
        String egnFromDB = this.personRepository.getEGNByEGN(egn);
        if (egnFromDB == null) {
            return false;
        }

        return true;
    }

    @Override
    public boolean doWeHavePersonWithEGNWhenUpdate(Long personId, String egn) {
        Long personIdFromDB = this.personRepository.getIdByEGN(egn);
        if (personIdFromDB == null || personIdFromDB.equals(personId)) {
            return false;
        }

        return true;
    }

    @Autowired
    public void setPersonRepository(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }
}
