package com.officemaneger.areas.annualLeaveType.entities;

import com.officemaneger.areas.dualNameTypes.entities.DualNameType;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "AnnualLeaveType")
public class AnnualLeaveType extends DualNameType {

    public AnnualLeaveType() {
    }
}
