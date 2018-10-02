package com.officemaneger.areas.phoneContact.services;

import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.phoneContact.entities.PhoneContact;
import com.officemaneger.areas.phoneContact.models.bindingModels.PhoneContactUpdateModel;
import com.officemaneger.areas.phoneContact.models.bindingModels.RegisterPhoneContact;
import com.officemaneger.areas.phoneContact.repositories.PhoneContactRepository;
import com.officemaneger.enums.PhoneType;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PhoneContactServiceImpl implements PhoneContactService {

    private PhoneContactRepository phoneContactRepository;

    private ModelMapper modelMapper;

    public PhoneContactServiceImpl() {
    }

    @Override
    public PhoneContact create(RegisterPhoneContact registerPhoneContact) {
        PhoneContact phoneContact = new PhoneContact();
        phoneContact.setDescription(registerPhoneContact.getDescription());
        phoneContact.setPhoneNumber(registerPhoneContact.getPhoneNumber());
        PhoneType phoneType = PhoneType.retrieveByLongBG(registerPhoneContact.getPhoneType());
        phoneContact.setPhoneType(phoneType);

        return this.phoneContactRepository.save(phoneContact);
    }

    @Override
    public void createOrUpdate(PhoneContactUpdateModel contact, Employee employee) {
        PhoneContact phoneContact = this.phoneContactRepository.findOne(contact.getId());
        if (phoneContact == null) {
            //new one
            phoneContact = new PhoneContact();
            phoneContact = this.phoneContactRepository.save(phoneContact);
        }

        phoneContact.setDescription(contact.getDescription());
        phoneContact.setPhoneNumber(contact.getPhoneNumber());
        PhoneType phoneType = PhoneType.retrieveByLongBG(contact.getPhoneType());
        phoneContact.setPhoneType(phoneType);
        employee.addPhoneNumber(phoneContact);
        phoneContact.setOwner(employee);
    }

    @Override
    public void deletePhoneNumber(Long phoneNumberId) {
        PhoneContact phoneContact = this.phoneContactRepository.findOne(phoneNumberId);
        if (phoneContact == null) {
            return;
        }
        if (phoneContact.getOwner() != null) {
            phoneContact.getOwner().removePhoneNumber(phoneContact);
            phoneContact.setOwner(null);
        }
        this.phoneContactRepository.delete(phoneNumberId);
    }

    @Autowired
    public void setPhoneContactRepository(PhoneContactRepository phoneContactRepository) {
        this.phoneContactRepository = phoneContactRepository;
    }

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
