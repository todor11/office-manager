package com.officemaneger.areas.shiftTypeGroup.entities;

import com.officemaneger.areas.shiftType.entities.ShiftType;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "shift_type_groups")
public class ShiftTypeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "group_name", unique = true)
    private String groupName;

    @OneToMany(mappedBy = "shiftTypeGroup", targetEntity = ShiftType.class)
    private List<ShiftType> shiftTypes;

    @Column(name = "group_order_weight")
    private int groupOrderWeight;

    public ShiftTypeGroup() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGroupName() {
        return this.groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public List<ShiftType> getShiftTypes() {
        return this.shiftTypes;
    }

    public void setShiftTypes(List<ShiftType> shiftTypes) {
        this.shiftTypes = shiftTypes;
    }

    public boolean addShiftType(ShiftType shiftType) {
        if (shiftType == null) {
            return false;
        }

        if (this.checkHasCollision(shiftType)) {
            return false;
        }

        this.shiftTypes.add(shiftType);
        return true;
    }

    public void removeShiftType(ShiftType shiftType) {
        this.shiftTypes.remove(shiftType);
    }

    private boolean checkHasCollision(ShiftType shiftType) {
        for (ShiftType type : this.shiftTypes) {
            if ((shiftType.getCanBeOnWorkDay() && type.getCanBeOnWorkDay()) ||
                    (shiftType.getCanBeOnRestDay() && type.getCanBeOnRestDay()) ||
                    (shiftType.getCanBeOnLastWorkDayBeforeRestDay() && type.getCanBeOnLastWorkDayBeforeRestDay()) ||
                    (shiftType.getCanBeOnLastRestDayBeforeWorkDay() && type.getCanBeOnLastRestDayBeforeWorkDay())) {
                if ((shiftType.getCanBeOnDoubleShiftRegime() && type.getCanBeOnDoubleShiftRegime()) ||
                        (shiftType.getCanBeOnTripleShiftRegime() && type.getCanBeOnTripleShiftRegime())) {
                    return true;
                }
            }

        }

        return false;
    }

    public int getGroupOrderWeight() {
        return this.groupOrderWeight;
    }

    public void setGroupOrderWeight(int groupOrderWeight) {
        this.groupOrderWeight = groupOrderWeight;
    }
}
