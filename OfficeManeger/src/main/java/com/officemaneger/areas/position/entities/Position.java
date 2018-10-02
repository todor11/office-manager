package com.officemaneger.areas.position.entities;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import com.officemaneger.areas.employee.entities.Employee;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "positions")
public class Position implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    private String name;

    @ManyToOne
    @JoinColumn(name = "unit_id", referencedColumnName = "id")
    private BusinessUnit businessUnit;

    @OneToOne(mappedBy = "position", targetEntity = Employee.class)
    private Employee employee;

    public Position() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
