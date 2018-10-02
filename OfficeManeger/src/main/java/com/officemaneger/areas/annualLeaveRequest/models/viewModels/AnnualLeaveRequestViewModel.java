package com.officemaneger.areas.annualLeaveRequest.models.viewModels;

import java.time.LocalDate;

public class AnnualLeaveRequestViewModel {

    private Long id;

    private String creationDateTime;

    private Long employeeId;

    private String employeeFullNameAndOfficiaryId;

    private String typeAndNumbOfDays;

    private LocalDate startDate;

    private LocalDate endDate;

    private long numbOfDays;

    private boolean isViewed;

    public AnnualLeaveRequestViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreationDateTime() {
        return this.creationDateTime;
    }

    public void setCreationDateTime(String creationDateTime) {
        this.creationDateTime = creationDateTime;
    }

    public String getEmployeeFullNameAndOfficiaryId() {
        return this.employeeFullNameAndOfficiaryId;
    }

    public void setEmployeeFullNameAndOfficiaryId(String employeeFullNameAndOfficiaryId) {
        this.employeeFullNameAndOfficiaryId = employeeFullNameAndOfficiaryId;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getTypeAndNumbOfDays() {
        return this.typeAndNumbOfDays;
    }

    public void setTypeAndNumbOfDays(String typeAndNumbOfDays) {
        this.typeAndNumbOfDays = typeAndNumbOfDays;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getNumbOfDays() {
        return this.numbOfDays;
    }

    public void setNumbOfDays(long numbOfDays) {
        this.numbOfDays = numbOfDays;
    }

    public boolean getIsViewed() {
        return this.isViewed;
    }

    public void setIsViewed(boolean isViewed) {
        this.isViewed = isViewed;
    }
}
