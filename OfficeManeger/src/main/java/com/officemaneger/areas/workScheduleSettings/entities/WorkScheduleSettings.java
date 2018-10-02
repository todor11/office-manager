package com.officemaneger.areas.workScheduleSettings.entities;

import com.officemaneger.areas.businessUnit.entities.BusinessUnit;
import com.officemaneger.areas.shiftType.entities.ShiftType;
import com.officemaneger.areas.shiftTypeGroup.entities.ShiftTypeGroup;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "work_schedule_settings")
public class WorkScheduleSettings implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private BusinessUnit businessUnit;

    @ManyToMany
    @JoinTable(name = "work_schedule_settings_shift_type_groups",
            joinColumns = @JoinColumn(name = "work_schedule_settings_id"),
            inverseJoinColumns = @JoinColumn(name = "shift_type_group_id"))
    private List<ShiftTypeGroup> shiftTypeGroupsToObserve;

    @Column(name = "numb_of_employees_in_shift")
    private int numbOfEmployeesInShift;

    @Column(name = "is_on_double_shift_regime")
    private boolean isOnDoubleShiftRegime;

    @Column(name = "is_on_triple_shift_regime")
    private boolean isOnTripleShiftRegime;

    public WorkScheduleSettings() {
        this.setShiftTypeGroupsToObserve(new ArrayList<>());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BusinessUnit getBusinessUnit() {
        return this.businessUnit;
    }

    public void setBusinessUnit(BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    public void addShiftTypeGroup(ShiftTypeGroup shiftTypeGroup) {
        if (shiftTypeGroup != null) {
            this.shiftTypeGroupsToObserve.add(shiftTypeGroup);
        }
    }

    public void removeShiftTypeGroup(ShiftTypeGroup shiftTypeGroup) {
        this.shiftTypeGroupsToObserve.remove(shiftTypeGroup);
    }

    public List<ShiftTypeGroup> getShiftTypeGroupsToObserve() {
        return this.shiftTypeGroupsToObserve;
    }

    public void setShiftTypeGroupsToObserve(List<ShiftTypeGroup> shiftTypeGroupsToObserve) {
        this.shiftTypeGroupsToObserve = shiftTypeGroupsToObserve;
    }

    public int getNumbOfEmployeesInShift() {
        return this.numbOfEmployeesInShift;
    }

    public void setNumbOfEmployeesInShift(int numbOfEmployeesInShift) {
        this.numbOfEmployeesInShift = numbOfEmployeesInShift;
    }

    public boolean getIsOnDoubleShiftRegime() {
        return this.isOnDoubleShiftRegime;
    }

    public void setIsOnDoubleShiftRegime(boolean isOnDoubleShiftRegime) {
        this.isOnDoubleShiftRegime = isOnDoubleShiftRegime;
    }

    public boolean getIsOnTripleShiftRegime() {
        return this.isOnTripleShiftRegime;
    }

    public void setIsOnTripleShiftRegime(boolean isOnTripleShiftRegime) {
        this.isOnTripleShiftRegime = isOnTripleShiftRegime;
    }
}
