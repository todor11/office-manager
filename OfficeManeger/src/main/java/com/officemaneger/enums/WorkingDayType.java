package com.officemaneger.enums;

public enum WorkingDayType {

    WORKDAY("workday", "работен"),
    RESTDAY("restday", "почивен"),
    NATIONALDAY("national day", "национален празник"),
    HOLIDAY("holiday", "допълнителен неработен");

    private String angl;
    private String bulg;

    WorkingDayType(String angl, String bulg) {
        this.angl = angl;
        this.bulg = bulg;
    }

    public String getAngl() {
        return this.angl;
    }

    public String getBulg() {
        return this.bulg;
    }
}
