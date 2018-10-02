package com.officemaneger.areas.annualLeaveDateInterval.entities;

import com.officemaneger.areas.annualLeaveRequest.entities.AnnualLeaveRequest;
import com.officemaneger.areas.annualLeaveType.entities.AnnualLeaveType;
import com.officemaneger.areas.dateInterval.entities.DateInterval;

import javax.persistence.*;

@Entity
@DiscriminatorValue(value = "AnnualLeaveDateInterval")
public class AnnualLeaveDateInterval extends DateInterval {

    @ManyToOne
    @JoinColumn(name = "type_id", referencedColumnName = "id")
    private AnnualLeaveType type;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "annual_leave_request_id", referencedColumnName = "id")
    private AnnualLeaveRequest annualLeaveRequest;

    public AnnualLeaveDateInterval() {
    }

    public AnnualLeaveType getType() {
        return this.type;
    }

    public void setType(AnnualLeaveType type) {
        this.type = type;
    }

    public AnnualLeaveRequest getAnnualLeaveRequest() {
        return this.annualLeaveRequest;
    }

    public void setAnnualLeaveRequest(AnnualLeaveRequest annualLeaveRequest) {
        this.annualLeaveRequest = annualLeaveRequest;
    }
}
