package com.officemaneger.areas.workScheduleSettings.models.viewModels;

import com.officemaneger.areas.shiftTypeGroup.models.viewModels.ShiftTypeGroupWorkScheduleSettingsViewModel;

import java.util.ArrayList;
import java.util.List;

public class WorkScheduleSettingsViewModel {

    private Long id;

    private List<ShiftTypeGroupWorkScheduleSettingsViewModel> allShiftTypeGroups;

    private int numbOfEmployeesInShift;

    private boolean isOnDoubleShiftRegime;

    private boolean isOnTripleShiftRegime;

    public WorkScheduleSettingsViewModel() {
        this.setAllShiftTypeGroups(new ArrayList<>());
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ShiftTypeGroupWorkScheduleSettingsViewModel> getAllShiftTypeGroups() {
        return this.allShiftTypeGroups;
    }

    public void setAllShiftTypeGroups(List<ShiftTypeGroupWorkScheduleSettingsViewModel> allShiftTypeGroups) {
        this.allShiftTypeGroups = allShiftTypeGroups;
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
