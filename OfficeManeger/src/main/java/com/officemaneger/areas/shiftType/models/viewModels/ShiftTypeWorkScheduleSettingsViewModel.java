package com.officemaneger.areas.shiftType.models.viewModels;

public class ShiftTypeWorkScheduleSettingsViewModel {

    private Long id;

    private String fullName;

    private boolean isInSettings;

    public ShiftTypeWorkScheduleSettingsViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean getIsInSettings() {
        return this.isInSettings;
    }

    public void setIsInSettings(boolean isInSettings) {
        this.isInSettings = isInSettings;
    }
}
