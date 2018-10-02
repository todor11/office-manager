package com.officemaneger.areas.workSchedule.models.viewModels;

import com.officemaneger.areas.employee.models.viewModels.EmployeeFullShortNameViewModel;
import com.officemaneger.areas.employeeShift.models.viewModels.EmployeeFullShortNameShiftViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkShiftByGroupAndDateViewModel {

    private String date;

    private String shortDate;

    private boolean isPayShift;

    private int numbOfHourToPay;

    private boolean isHoliday;

    private boolean isToday;

    private boolean isFull;

    private int numbOfEmployeesInShift;

    private Long shiftTypeId;

    private String shiftTypeShortName;

    private boolean hasNoPossibleShiftOnThisDate;

    private List<EmployeeFullShortNameShiftViewModel> employees;

    public WorkShiftByGroupAndDateViewModel() {
        this.setEmployees(new ArrayList<>());
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShortDate() {
        return this.shortDate;
    }

    public void setShortDate(String shortDate) {
        this.shortDate = shortDate;
    }

    public boolean getIsPayShift() {
        return this.isPayShift;
    }

    public void setIsPayShift(boolean isPayShift) {
        this.isPayShift = isPayShift;
    }

    public int getNumbOfHourToPay() {
        return this.numbOfHourToPay;
    }

    public void setNumbOfHourToPay(int numbOfHourToPay) {
        this.numbOfHourToPay = numbOfHourToPay;
    }

    public boolean getIsHoliday() {
        return this.isHoliday;
    }

    public void setIsHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public boolean getIsToday() {
        return this.isToday;
    }

    public void setIsToday(boolean isToday) {
        this.isToday = isToday;
    }

    public boolean getIsFull() {
        return this.isFull;
    }

    public void setIsFull(boolean isFull) {
        this.isFull = isFull;
    }

    public int getNumbOfEmployeesInShift() {
        return this.numbOfEmployeesInShift;
    }

    public void setNumbOfEmployeesInShift(int numbOfEmployeesInShift) {
        this.numbOfEmployeesInShift = numbOfEmployeesInShift;
    }

    public List<EmployeeFullShortNameShiftViewModel> getEmployees() {
        return this.employees;
    }

    public void setEmployees(List<EmployeeFullShortNameShiftViewModel> employees) {
        this.employees = employees;
    }

    public Long getShiftTypeId() {
        return this.shiftTypeId;
    }

    public void setShiftTypeId(Long shiftTypeId) {
        this.shiftTypeId = shiftTypeId;
    }

    public boolean getHasNoPossibleShiftOnThisDate() {
        return this.hasNoPossibleShiftOnThisDate;
    }

    public void setHasNoPossibleShiftOnThisDate(boolean hasNoPossibleShiftOnThisDate) {
        this.hasNoPossibleShiftOnThisDate = hasNoPossibleShiftOnThisDate;
    }

    public String getShiftTypeShortName() {
        return this.shiftTypeShortName;
    }

    public void setShiftTypeShortName(String shiftTypeShortName) {
        this.shiftTypeShortName = shiftTypeShortName;
    }
}
