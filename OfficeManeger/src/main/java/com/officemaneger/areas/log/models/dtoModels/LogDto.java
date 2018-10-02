package com.officemaneger.areas.log.models.dtoModels;


import java.time.LocalDateTime;

public class LogDto {

    private LocalDateTime date;

    private String username;

    private String userIP;

    private String className;

    private String classMethod;

    private long actionExecution;

    private long overallExecution;

    private boolean isGetPage;

    private boolean isInvalidCredentials;

    public LogDto() {
    }

    public LogDto(String username, String userIP, String className, String classMethod,
                  long actionExecution, long overallExecution, boolean isGetPage, boolean isInvalidCredentials) {
        this.username = username;
        this.userIP = userIP;
        this.className = className;
        this.classMethod = classMethod;
        this.actionExecution = actionExecution;
        this.overallExecution = overallExecution;
        this.isGetPage = isGetPage;
        this.isInvalidCredentials = isInvalidCredentials;
        this.date = LocalDateTime.now();
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getUserIP() {
        return userIP;
    }

    public void setUserIP(String userIP) {
        this.userIP = userIP;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClassMethod() {
        return classMethod;
    }

    public void setClassMethod(String classMethod) {
        this.classMethod = classMethod;
    }

    public long getActionExecution() {
        return actionExecution;
    }

    public void setActionExecution(long actionExecution) {
        this.actionExecution = actionExecution;
    }

    public long getOverallExecution() {
        return overallExecution;
    }

    public void setOverallExecution(long overallExecution) {
        this.overallExecution = overallExecution;
    }

    public boolean getIsGetPage() {
        return isGetPage;
    }

    public void setIsGetPage(boolean isGetPage) {
        this.isGetPage = isGetPage;
    }

    public boolean getIsInvalidCredentials() {
        return isInvalidCredentials;
    }

    public void setInvalidCredentials(boolean isInvalidCredentials) {
        this.isInvalidCredentials = isInvalidCredentials;
    }
}
