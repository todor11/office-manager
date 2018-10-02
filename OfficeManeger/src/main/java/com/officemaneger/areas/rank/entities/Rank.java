package com.officemaneger.areas.rank.entities;

import com.officemaneger.areas.employee.entities.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ranks")
public class Rank implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true)
    private String name;

    @Column(name = "rank_rate")
    private Integer rankRate;

    @OneToMany(mappedBy = "rank", targetEntity = Employee.class)
    private List<Employee> employees;

    public Rank() {
        this.employees = new ArrayList<>();
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

    public Integer getRankRate() {
        return rankRate;
    }

    public void setRankRate(Integer rankRate) {
        this.rankRate = rankRate;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public void addEmployee(Employee employee) {
        if (employee != null) {
            this.employees.add(employee);
        }
    }

    public void removeEmployee(Employee employee) {
        this.employees.remove(employee);
    }
}
