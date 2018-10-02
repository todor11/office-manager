package com.officemaneger.areas.employeeShift.models.bindingModels;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class EmployeeShiftInWorkShiftUpdateModel {

    @NotNull(message = "Id на смяната не може да е празно поле.")
    private Long id;

    @NotNull(message = "Id на типа на смяната не може да е празно поле.")
    private Long shiftTypeId;

    @NotNull(message = "Id на служителя не може да е празно поле.")
    private Long employeeId;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime startTime;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime endTime;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime secondStartTime;

    @JsonFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime secondEndTime;

    private boolean isEndOnNextDay;

    public EmployeeShiftInWorkShiftUpdateModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getShiftTypeId() {
        return this.shiftTypeId;
    }

    public void setShiftTypeId(Long shiftTypeId) {
        this.shiftTypeId = shiftTypeId;
    }

    public Long getEmployeeId() {
        return this.employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDateTime getStartTime() {
        return this.startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return this.endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public LocalDateTime getSecondStartTime() {
        return this.secondStartTime;
    }

    public void setSecondStartTime(LocalDateTime secondStartTime) {
        this.secondStartTime = secondStartTime;
    }

    public LocalDateTime getSecondEndTime() {
        return this.secondEndTime;
    }

    public void setSecondEndTime(LocalDateTime secondEndTime) {
        this.secondEndTime = secondEndTime;
    }

    public boolean getIsEndOnNextDay() {
        return this.isEndOnNextDay;
    }

    public void setIsEndOnNextDay(boolean isEndOnNextDay) {
        this.isEndOnNextDay = isEndOnNextDay;
    }
}
