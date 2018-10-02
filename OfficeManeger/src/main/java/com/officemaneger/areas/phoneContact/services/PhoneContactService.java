package com.officemaneger.areas.phoneContact.services;

import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.phoneContact.entities.PhoneContact;
import com.officemaneger.areas.phoneContact.models.bindingModels.PhoneContactUpdateModel;
import com.officemaneger.areas.phoneContact.models.bindingModels.RegisterPhoneContact;

public interface PhoneContactService {

    PhoneContact create(RegisterPhoneContact registerPhoneContact);

    void createOrUpdate(PhoneContactUpdateModel contact, Employee employee);

    void deletePhoneNumber(Long phoneNumberId);
}
