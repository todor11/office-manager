package com.officemaneger.areas.workScheduleSettings.models.bindingModels;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class WorkScheduleSettingsUpdateModel {

    @NotNull(message = "error.workScheduleSettings.id")
    private Long id;

    private List<Long> shiftTypeGroupsToObserveIds;

    @Min(value = 1, message = "error.workScheduleSettingsUpdate.invalidNumbOfEmployees")
    private int numbOfEmployeesInShift;

    private boolean isOnDoubleShiftRegime;

    private boolean isOnTripleShiftRegime;

    public WorkScheduleSettingsUpdateModel() {
        this.setShiftTypeGroupsToObserveIds(new ArrayList<>());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Long> getShiftTypeGroupsToObserveIds() {
        return this.shiftTypeGroupsToObserveIds;
    }

    public void setShiftTypeGroupsToObserveIds(List<Long> shiftTypeGroupsToObserveIds) {
        this.shiftTypeGroupsToObserveIds = shiftTypeGroupsToObserveIds;
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
