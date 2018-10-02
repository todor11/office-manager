package com.officemaneger.areas.employee.entities;

import com.officemaneger.areas.annualLeave.entities.AnnualLeave;
import com.officemaneger.areas.annualLeaveRequest.entities.AnnualLeaveRequest;
import com.officemaneger.areas.employeeShift.entities.EmployeeShift;
import com.officemaneger.areas.person.entities.Person;
import com.officemaneger.areas.position.entities.Position;
import com.officemaneger.areas.rank.entities.Rank;
import com.officemaneger.areas.user.entities.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue(value = "Employee")
public class Employee extends Person {

    @Column(name = "officiary_ID", unique = true)
    private String officiaryID;

    @OneToOne
    private Position position;

    @ManyToOne
    @JoinColumn(name = "rank_id", referencedColumnName = "id")
    private Rank rank;

    @OneToMany(mappedBy = "owner", targetEntity = EmployeeShift.class, cascade = CascadeType.ALL)
    private List<EmployeeShift> shifts;

    @OneToMany(mappedBy = "owner", targetEntity = AnnualLeave.class, cascade = CascadeType.ALL)
    private List<AnnualLeave> annualLeaves;

    @Column(name = "is_active")
    private boolean isActive;

    @OneToMany(mappedBy = "employee", targetEntity = User.class, cascade = CascadeType.ALL)
    private List<User> accounts;

    @OneToMany(mappedBy = "employee", targetEntity = AnnualLeaveRequest.class, cascade = CascadeType.ALL)
    private List<AnnualLeaveRequest> annualLeaveRequests;

    public Employee() {
        this.shifts = new ArrayList<>();
        this.annualLeaves = new ArrayList<>();
        this.accounts = new ArrayList<>();
        this.annualLeaveRequests = new ArrayList<>();
    }

    public String getOfficiaryID() {
        return officiaryID;
    }

    public void setOfficiaryID(String officiaryID) {
        this.officiaryID = officiaryID;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public List<EmployeeShift> getShifts() {
        return shifts;
    }

    public void setShifts(List<EmployeeShift> shifts) {
        this.shifts = shifts;
    }

    public void addShift(EmployeeShift employeeShift) {
        if (employeeShift != null) {
            this.shifts.add(employeeShift);
        }
    }

    public void removeShift(EmployeeShift employeeShift) {
        this.shifts.remove(employeeShift);
    }

    public List<AnnualLeave> getAnnualLeaves() {
        return annualLeaves;
    }

    public void setAnnualLeaves(List<AnnualLeave> annualLeaves) {
        this.annualLeaves = annualLeaves;
    }

    public void addAnnualLeave(AnnualLeave annualLeave) {
        if (annualLeave != null) {
            this.annualLeaves.add(annualLeave);
        }
    }

    public void removeAnnualLeave(AnnualLeave annualLeave) {
        this.annualLeaves.remove(annualLeave);
    }

    public boolean getIsActive() {
        return this.isActive;
    }

    public void setIsActive(boolean isActive) {
        this.isActive = isActive;

        //set account too
        if (this.getAccounts() != null) {
            for (User account : this.accounts) {
                account.setEnabled(isActive);
            }
        }
    }

    public List<User> getAccounts() {
        return this.accounts;
    }

    public void setAccounts(List<User> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(User account) {
        if (account != null) {
            this.accounts.add(account);
        }
    }

    public void removeAccount(User account) {
        this.accounts.remove(account);
    }

    public List<AnnualLeaveRequest> getAnnualLeaveRequests() {
        return this.annualLeaveRequests;
    }

    public void setAnnualLeaveRequests(List<AnnualLeaveRequest> annualLeaveRequests) {
        this.annualLeaveRequests = annualLeaveRequests;
    }
}
