package com.officemaneger.enums;

public enum PhoneType {

    OFFICIARY("officiary", "officiary phone", "служебен", "служебен телефон"),
    PERSONAL("personal", "personal phone", "личен", "личен телефон"),
    FAMILY("family", "family phone", "семеен", "семеен телефон"),
    МAIN("main", "main phone", "основен", "основен телефон");

    private String shortEN;
    private String longEN;
    private String shortBG;
    private String longBG;

    PhoneType(String shortEN, String longEN, String shortBG, String longBG) {
        this.shortEN = shortEN;
        this.longEN = longEN;
        this.shortBG = shortBG;
        this.longBG = longBG;
    }

    public String getShortEN() {
        return shortEN;
    }

    public String getLongEN() {
        return longEN;
    }

    public String getShortBG() {
        return shortBG;
    }

    public String getLongBG() {
        return longBG;
    }

    public static PhoneType retrieveByLongBG(String longBG) {
        switch (longBG) {
            case "служебен телефон":
                return PhoneType.OFFICIARY;
            case "личен телефон":
                return PhoneType.PERSONAL;
            case "семеен телефон":
                return PhoneType.FAMILY;
            case "основен телефон":
                return PhoneType.МAIN;
            default:
                return null;
        }
    }
}
