package com.officemaneger.areas.workShift.entities;

import com.officemaneger.areas.employeeShift.entities.EmployeeShift;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
/*
@Entity
@Table(name = "work_shifts")
public class WorkShift implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Basic
    private LocalDate date;

    @OneToMany(mappedBy = "workShift", targetEntity = EmployeeShift.class, cascade = CascadeType.ALL)
    private List<EmployeeShift> employeeShifts;

    public WorkShift() {
        this.setEmployeeShifts(new ArrayList<>());
    }

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<EmployeeShift> getEmployeeShifts() {
        return this.employeeShifts;
    }

    public void setEmployeeShifts(List<EmployeeShift> employeeShifts) {
        this.employeeShifts = employeeShifts;
    }

    public void addEmployeeShift(EmployeeShift employeeShift) {
        if (employeeShift != null) {
            this.employeeShifts.add(employeeShift);
        }
    }

    public void removeEmployeeShift(EmployeeShift employeeShift) {
        this.employeeShifts.remove(employeeShift);
    }
}

*/