package com.officemaneger.areas.annualLeave.entities;

import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.employee.entities.Employee;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "annual_leaves", uniqueConstraints = { @UniqueConstraint(columnNames =
        { "type_id", "owner_id" }) })
public class AnnualLeave implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private AnnualLeaveType type;

    @Column(name = "number_of_annual_leave")
    private int numberOfAnnualLeave;

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private Employee owner;

    public AnnualLeave() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AnnualLeaveType getType() {
        return type;
    }

    public void setType(AnnualLeaveType type) {
        this.type = type;
    }

    public int getNumberOfAnnualLeave() {
        return numberOfAnnualLeave;
    }

    public void setNumberOfAnnualLeave(int numberOfAnnualLeave) {
        this.numberOfAnnualLeave = numberOfAnnualLeave;
    }

    public Employee getOwner() {
        return owner;
    }

    public void setOwner(Employee owner) {
        this.owner = owner;
    }

}