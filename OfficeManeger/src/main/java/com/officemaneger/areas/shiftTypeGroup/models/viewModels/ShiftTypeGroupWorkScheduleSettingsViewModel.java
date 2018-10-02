package com.officemaneger.areas.shiftTypeGroup.models.viewModels;

public class ShiftTypeGroupWorkScheduleSettingsViewModel {

    private Long id;

    private String groupName;

    private boolean isInSettings;

    public ShiftTypeGroupWorkScheduleSettingsViewModel() {
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

    public boolean getIsInSettings() {
        return this.isInSettings;
    }

    public void setIsInSettings(boolean isInSettings) {
        this.isInSettings = isInSettings;
    }

}
