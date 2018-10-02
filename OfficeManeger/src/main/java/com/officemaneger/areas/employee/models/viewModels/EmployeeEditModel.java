package com.officemaneger.areas.employee.models.viewModels;

import com.officemaneger.areas.phoneContact.models.viewModels.PhoneContactViewModel;

import java.util.ArrayList;
import java.util.List;

public class EmployeeEditModel {

    private Long id;

    private String firstName;

    private String middleName;

    private String lastName;

    private String egn;

    private String officiaryID;

    private String address;

    private List<PhoneContactViewModel> phoneNumbers;

    private Long rankId;

    private boolean isActive;

    public EmployeeEditModel() {
        this.phoneNumbers = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEgn() {
        return egn;
    }

    public void setEgn(String egn) {
        this.egn = egn;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<PhoneContactViewModel> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<PhoneContactViewModel> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public Long getRankId() {
        return this.rankId;
    }

    public void setRankId(Long rankId) {
        this.rankId = rankId;
    }

    public String getOfficiaryID() {
        return officiaryID;
    }

    public void setOfficiaryID(String officiaryID) {
        this.officiaryID = officiaryID;
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
