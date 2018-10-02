package com.officemaneger.enums;

public enum Gender {

    MALE ("male", "Male", "m", "M", "мъж", "Мъж", "м", "М"),
    FEMALE ("female", "Female", "f", "F", "жена", "Жена", "ж", "Ж");

    private String longLower;
    private String longUpper;
    private String shortLower;
    private String shortUpper;
    private String longLowerBG;
    private String longUpperBG;
    private String shortLowerBG;
    private String shortUpperBG;

    Gender(String longLower, String longUpper, String shortLower, String shortUpper,
           String longLowerBG, String longUpperBG, String shortLowerBG, String shortUpperBG) {
        this.longLower = longLower;
        this.longUpper = longUpper;
        this.shortLower = shortLower;
        this.shortUpper = shortUpper;
        this.longLowerBG = longLowerBG;
        this.longUpperBG = longUpperBG;
        this.shortLowerBG = shortLowerBG;
        this.shortUpperBG = shortUpperBG;
    }

    public String getLongLower() {
        return this.longLower;
    }

    public String getLongUpper() {
        return this.longUpper;
    }

    public String getShortLower() {
        return this.shortLower;
    }

    public String getShortUpper() {
        return this.shortUpper;
    }

    public String getLongLowerBG() {
        return this.longLowerBG;
    }

    public String getLongUpperBG() {
        return this.longUpperBG;
    }

    public String getShortLowerBG() {
        return this.shortLowerBG;
    }

    public String getShortUpperBG() {
        return this.shortUpperBG;
    }
}
