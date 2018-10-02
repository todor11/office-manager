package com.officemaneger.areas.annualLeaveRequest.entities;

import com.officemaneger.areas.annualLeaveDateInterval.entities.AnnualLeaveDateInterval;
import com.officemaneger.areas.employee.entities.Employee;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "annual_leave_request")
public class AnnualLeaveRequest implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;

    @OneToMany(mappedBy = "annualLeaveRequest", targetEntity = AnnualLeaveDateInterval.class, cascade = CascadeType.ALL)
    private List<AnnualLeaveDateInterval> annualLeaveDateIntervals;

    @Column(name = "is_viewed")
    private boolean isViewed;

    @Column(name = "is_approved")
    private boolean isApproved;

    @Column(name = "creation_date_time")
    private LocalDateTime creationDateTime;

    @Column(name = "execution_date_time")
    private LocalDateTime executionDateTime;

    public AnnualLeaveRequest() {
        this.annualLeaveDateIntervals = new ArrayList<>();
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<AnnualLeaveDateInterval> getAnnualLeaveDateIntervals() {
        return this.annualLeaveDateIntervals;
    }

    public void setAnnualLeaveDateIntervals(List<AnnualLeaveDateInterval> annualLeaveDateIntervals) {
        this.annualLeaveDateIntervals = annualLeaveDateIntervals;
    }

    public void addAnnualLeaveDateInterval(AnnualLeaveDateInterval annualLeaveDateInterval) {
        if (annualLeaveDateInterval != null) {
            this.annualLeaveDateIntervals.add(annualLeaveDateInterval);
        }
    }

    public void removeAnnualLeaveDateInterval(AnnualLeaveDateInterval annualLeaveDateInterval) {
        this.annualLeaveDateIntervals.remove(annualLeaveDateInterval);
    }

    public boolean getIsViewed() {
        return this.isViewed;
    }

    public void setIsViewed(boolean isViewed) {
        this.isViewed = isViewed;
    }

    public boolean getIsApproved() {
        return this.isApproved;
    }

    public void setIsApproved(boolean isApproved) {
        this.isApproved = isApproved;
    }

    public LocalDateTime getCreationDateTime() {
        return this.creationDateTime;
    }

    public void setCreationDateTime(LocalDateTime creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public LocalDateTime getExecutionDateTime() {
        return this.executionDateTime;
    }

    public void setExecutionDateTime(LocalDateTime executionDateTime) {
        this.executionDateTime = executionDateTime;
    }
}
