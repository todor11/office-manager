package com.officemaneger.areas.employee.models.bindingModels;

import com.officemaneger.enums.Gender;
import com.officemaneger.areas.phoneContact.models.bindingModels.RegisterPhoneContact;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class CreateEmployeeModel {

    @NotEmpty(message = "error.employee.firstName")
    private String firstName;

    private String middleName;

    @NotEmpty(message = "error.employee.lastName")
    private String lastName;

    //@NotEmpty(message = "error.employee.emptyEGN")
    //@Size(min = 10, max = 10, message = "error.employee.shortEGN")
    private String egn;

    @NotEmpty(message = "error.employee.emptyOfficiaryID")
    private String officiaryID;

    private String address;

    private List<RegisterPhoneContact> phoneNumbers;

    private Long rankId;

    public CreateEmployeeModel() {
        this.phoneNumbers = new ArrayList<>();
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

    public List<RegisterPhoneContact> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<RegisterPhoneContact> phoneNumbers) {
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
}
