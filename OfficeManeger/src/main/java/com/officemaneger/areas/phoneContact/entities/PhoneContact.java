package com.officemaneger.areas.phoneContact.entities;

import com.officemaneger.areas.person.entities.Person;
import com.officemaneger.enums.PhoneType;

import javax.persistence.*;

@Entity
@Table(name = "phone_contacts")
public class PhoneContact {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "phone_type")
    @Enumerated(EnumType.STRING)
    private PhoneType phoneType;

    @Column(name = "phone_description")
    private String description;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Person owner;

    @Column(name = "is_firm_registered_phone")
    private boolean isFirmRegisteredPhone;

    public PhoneContact() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person ouner) {
        this.owner = ouner;
    }

    public PhoneType getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(PhoneType phoneType) {
        this.phoneType = phoneType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsFirmRegisteredPhone() {
        return this.isFirmRegisteredPhone;
    }

    public void setIsFirmRegisteredPhone(boolean isFirmRegisteredPhone) {
        this.isFirmRegisteredPhone = isFirmRegisteredPhone;
    }
}
