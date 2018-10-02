package com.officemaneger.areas.employee.models.viewModels;

import java.time.LocalDate;

public class EmployeeFullAdminView {

    private Long id;

    private String fullName;

    private String officiaryID;

    private String rank;

    private String EGN;

    private LocalDate birthDate;

    private String phoneNumbers;

    private boolean isActive;

    public EmployeeFullAdminView() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getOfficiaryID() {
        return officiaryID;
    }

    public void setOfficiaryID(String officiaryID) {
        this.officiaryID = officiaryID;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getEGN() {
        return EGN;
    }

    public void setEGN(String EGN) {
        this.EGN = EGN;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(String phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;
    }
}
