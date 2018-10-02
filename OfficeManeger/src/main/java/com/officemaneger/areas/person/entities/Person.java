package com.officemaneger.areas.person.entities;

import com.officemaneger.areas.phoneContact.entities.PhoneContact;
import com.officemaneger.enums.Gender;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", discriminatorType = DiscriminatorType.STRING)
public abstract class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "EGN", unique = true)
    private String EGN;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "middle_name")
    private String middleName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "full_short_name", unique = true)
    private String fullShortName;

    @Column(name = "full_name")
    private String fullName;

    @Basic
    private String address;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @OneToMany(mappedBy = "owner", targetEntity = PhoneContact.class, cascade = CascadeType.ALL)
    private List<PhoneContact> phoneNumbers;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    public Person() {
        this.phoneNumbers = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEGN() {
        return EGN;
    }

    public void setEGN(String EGN) {
        this.EGN = EGN;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        this.setTempFullName();
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
        this.setTempFullName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        this.setTempFullName();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<PhoneContact> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneContact> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public void addPhoneNumber(PhoneContact phoneContact) {
        if (phoneContact != null) {
            this.phoneNumbers.add(phoneContact);
        }
    }

    public void removePhoneNumber(PhoneContact phoneContact) {
        this.phoneNumbers.remove(phoneContact);
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getFullShortName() {
        return fullShortName;
    }

    public void setFullShortName(String fullShortName) {
        this.fullShortName = fullShortName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    private void setTempFullName() {
        String fullName = "";
        if (this.firstName != null) {
            fullName = this.firstName;
        }
        if (this.middleName != null) {
            fullName = fullName + " " + this.middleName;
        }
        if (this.lastName != null) {
            fullName = fullName + " " + this.lastName;
        }

        this.fullName = fullName;
    }
}
