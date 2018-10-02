package com.officemaneger.areas.businessUnit.entities;

import com.officemaneger.areas.computers.entities.Computer;
import com.officemaneger.areas.employee.entities.Employee;
import com.officemaneger.areas.position.entities.Position;
import com.officemaneger.areas.workScheduleSettings.entities.WorkScheduleSettings;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "business_units")
public class BusinessUnit implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "unit_name", unique = true)
    private String unitName;

    @ManyToOne
    @JoinColumn(name = "main_unit_id", referencedColumnName = "id")
    private BusinessUnit mainUnit;

    @ManyToOne
    @JoinColumn(name = "boss_position_id", referencedColumnName = "id")
    private Position bossPosition;

    @OneToMany(mappedBy = "businessUnit", targetEntity = Position.class, cascade = CascadeType.ALL)
    private List<Position> positions;

    @OneToMany(mappedBy = "mainUnit", targetEntity = BusinessUnit.class)
    private List<BusinessUnit> subUnits;

    @OneToMany(mappedBy = "businessUnit", targetEntity = Computer.class)
    private List<Computer> computers;

    @OneToOne(mappedBy = "businessUnit", targetEntity = WorkScheduleSettings.class, cascade = CascadeType.ALL)
    private WorkScheduleSettings workScheduleSettings;

    public BusinessUnit() {
        this.positions = new ArrayList<>();
        this.subUnits = new ArrayList<>();
    }

    public BusinessUnit(String unitName) {
        this();
        this.unitName = unitName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public BusinessUnit getMainUnit() {
        return mainUnit;
    }

    public void setMainUnit(BusinessUnit mainUnit) {
        this.mainUnit = mainUnit;
    }

    public Position getBossPosition() {
        return bossPosition;
    }

    public void setBossPosition(Position bossPosition) {
        this.bossPosition = bossPosition;
    }

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public List<BusinessUnit> getSubUnits() {
        return subUnits;
    }

    public void setSubUnits(List<BusinessUnit> subUnits) {
        this.subUnits = subUnits;
    }

    public void addEmployeePosition(Position position) {
        if (position != null) {
            this.positions.add(position);
        }
    }

    public void removeEmployeePosition(Position position) {
        if (position != null) {
            this.positions.remove(position);
        }
    }

    public void addSubUnit(BusinessUnit subUnit) {
        if (subUnit != null) {
            this.subUnits.add(subUnit);
        }
    }

    public void removeSubUnit(BusinessUnit subUnit) {
        if (subUnit != null) {
            this.subUnits.remove(subUnit);
        }
    }

    public List<Computer> getComputers() {
        return this.computers;
    }

    public void setComputers(List<Computer> computers) {
        this.computers = computers;
    }

    public void addComputer(Computer computer) {
        if (computer != null) {
            this.computers.add(computer);
        }
    }

    public void removeComputer(Computer computer) {
        this.computers.remove(computer);
    }

    public WorkScheduleSettings getWorkScheduleSettings() {
        return this.workScheduleSettings;
    }

    public void setWorkScheduleSettings(WorkScheduleSettings workScheduleSettings) {
        this.workScheduleSettings = workScheduleSettings;
    }
}
