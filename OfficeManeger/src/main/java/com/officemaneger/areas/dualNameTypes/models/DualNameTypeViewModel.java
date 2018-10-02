package com.officemaneger.areas.dualNameTypes.models;

public class DualNameTypeViewModel {

    private Long id;

    private String fullName;

    private String shortName;

    private boolean isRegular;

    public DualNameTypeViewModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getShortName() {
        return this.shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public boolean getIsRegular() {
        return this.isRegular;
    }

    public void setIsRegular(boolean isRegular) {
        this.isRegular = isRegular;
    }
}
