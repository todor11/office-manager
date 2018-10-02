package com.officemaneger.areas.phoneContact.repositories;

import com.officemaneger.areas.phoneContact.entities.PhoneContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneContactRepository extends JpaRepository<PhoneContact, Long> {
}
