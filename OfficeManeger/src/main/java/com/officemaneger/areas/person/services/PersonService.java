package com.officemaneger.areas.person.services;

public interface PersonService {

    boolean doWeHavePersonWithEGN(String egn);

    boolean doWeHavePersonWithEGNWhenUpdate(Long personId, String egn);
}
